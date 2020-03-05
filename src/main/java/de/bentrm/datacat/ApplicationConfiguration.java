package de.bentrm.datacat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
