package com.cardprocessor.config;

import com.cardprocessor.service.CreditCardValidator;
import com.cardprocessor.service.LuhnCreditCardValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.cardprocessor.service"})
public class ServiceConfig {

    @Bean
    public CreditCardValidator creditCardValidator() {
        return new LuhnCreditCardValidator();
    }
}
