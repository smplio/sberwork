package ua.com.lhjlbjyjd.homework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.com.lhjlbjyjd.homework.entities.Currency;
import ua.com.lhjlbjyjd.homework.services.CurrencyService;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Currency> getTodayStockExchange() {
        return List.of(currencyService.getTodayStockExchange());
    }

    @RequestMapping(value = "/{count}",method = RequestMethod.GET)
    public List<Currency> getLastStocks(@PathVariable("count") int n) {
        return currencyService.getLastStocks(n);
    }
}
