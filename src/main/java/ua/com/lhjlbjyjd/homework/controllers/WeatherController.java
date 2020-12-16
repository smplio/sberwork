package ua.com.lhjlbjyjd.homework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.lhjlbjyjd.homework.entities.Weather;
import ua.com.lhjlbjyjd.homework.services.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Weather> getLastWeather(
            @RequestParam(value = "days", required = false) Integer n,
            @RequestParam(value = "city", required = false) String city
    ) {
        if (n == null && city == null) {
            return List.of(weatherService.getTodayWeather());
        }
        if (n == null) {
            return List.of(weatherService.getTodayWeather(city));
        }
        if (city == null) {
            return weatherService.getLastWeather(n);
        }
        return weatherService.getLastWeather(n, city);
    }
}
