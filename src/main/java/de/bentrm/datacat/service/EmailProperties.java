package de.bentrm.datacat.service;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@Data
@Component
@ConfigurationProperties(prefix = "datacat.mail")
@Validated
public class EmailProperties {

    @URL
    private String homeUrl;

    @URL
    private String confirmUrl;

    @Email
    private String confirmFrom;

}
