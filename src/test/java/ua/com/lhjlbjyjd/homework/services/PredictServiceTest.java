package ua.com.lhjlbjyjd.homework.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.com.lhjlbjyjd.homework.entities.Currency;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PredictServiceTest {

    @Autowired
    private PredictService predictService;

    @Test
    public void getPrediction() {
        Currency result = predictService.getPrediction();
        assertEquals(LocalDate.now().plusDays(1), result.getDate());
    }
}