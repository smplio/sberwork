package ua.com.lhjlbjyjd.homework.dao;

import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ua.com.lhjlbjyjd.homework.entities.Currency;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class CurrencyDAO {
    private static final HashMap<LocalDate, Currency> currencyCache = new HashMap<>();

    public List<Currency> getLastExchangeRates(int n) {
        LocalDate currentDate = LocalDate.now();
        List<Currency> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            LocalDate date = currentDate.minusDays(i);
            if (currencyCache.containsKey(date)) {
                result.add(currencyCache.get(date));
            } else {
                String address = String.format(
                        "http://www.cbr.ru/scripts/XML_daily.asp?date_req=%s",
                        date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(address))
                        .build();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenApply(it -> parseDollarFromXml(it, date))
                        .thenApply(it -> {
                            currencyCache.put(date, it);
                            return it;
                        })
                        .thenAccept(result::add)
                        .join();
            }
        }
        return result;
    }

    private Currency parseDollarFromXml(String xml, LocalDate date) {
        try {
            final String DOLLAR_CODE = "840";
            final String NumCodeKey = "NumCode";
            final String NominalKey = "Nominal";
            final String NameKey = "Name";
            final String ValueKey = "Value";
            boolean dollarParsed = false;
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList list = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                NodeList mainItemList = list.item(i).getChildNodes();
                int nominal = 1;
                float value = 0;
                String name = "";
                for (int j = 0; j < mainItemList.getLength(); j++) {
                    Node item = mainItemList.item(j);
                    if (item.getNodeName().equals(NumCodeKey)) {
                        if (!item.getTextContent().equals(DOLLAR_CODE)) {
                            break;
                        } else {
                            dollarParsed = true;
                        }
                    }
                    if (item.getNodeName().equals(NominalKey)) {
                        nominal = Integer.parseInt(item.getTextContent());
                    }
                    if (item.getNodeName().equals(ValueKey)) {
                        value = Float.parseFloat(item.getTextContent().replace(',', '.'));
                    }
                    if (item.getNodeName().equals(NameKey)) {
                        name = item.getTextContent();
                    }
                }
                if (dollarParsed) {
                    assert nominal != 0;
                    return new Currency(name, value / nominal, date);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
