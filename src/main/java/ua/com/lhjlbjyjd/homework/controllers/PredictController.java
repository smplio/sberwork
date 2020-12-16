package ua.com.lhjlbjyjd.homework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.com.lhjlbjyjd.homework.entities.Currency;
import ua.com.lhjlbjyjd.homework.services.PredictService;

@RestController
@RequestMapping("/predict")
public class PredictController {

    @Autowired
    private PredictService predictService;

    @RequestMapping(method = RequestMethod.GET)
    public Currency getPrediction() {
        return predictService.getPrediction();
    }
}
