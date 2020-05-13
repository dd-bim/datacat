package de.bentrm.datacat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Field;
import java.util.Optional;

@Configuration
public class LoggingConfiguration {

    @Bean
    @Scope("prototype")
    public Logger logger(final InjectionPoint ip) {
        return LoggerFactory.getLogger(Optional.ofNullable(ip.getMethodParameter())
                .<Class>map(MethodParameter::getContainingClass)
                .orElseGet( () ->
                        Optional.ofNullable(ip.getField())
                                .map(Field::getDeclaringClass)
                                .orElseThrow (IllegalArgumentException::new)
                )
        );
    }

}
