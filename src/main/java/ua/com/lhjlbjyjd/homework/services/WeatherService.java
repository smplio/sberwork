package ua.com.lhjlbjyjd.homework.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.lhjlbjyjd.homework.dao.WeatherDAO;
import ua.com.lhjlbjyjd.homework.entities.Weather;

import java.util.List;

@Service
public class WeatherService {

    private static final String DEFAULT_CITY = "Moscow";

    @Autowired
    private WeatherDAO weatherDAO;

    public Weather getTodayWeather() {
        return getTodayWeather(DEFAULT_CITY);
    }

    public Weather getTodayWeather(String city) {
        return weatherDAO.getLastWeather(1, city).get(0);
    }

    public List<Weather> getLastWeather(int n) {
        return getLastWeather(n, DEFAULT_CITY);
    }

    public List<Weather> getLastWeather(int n, String city) {
        return weatherDAO.getLastWeather(n, city);
    }
}
