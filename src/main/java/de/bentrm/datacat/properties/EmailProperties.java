package de.bentrm.datacat.properties;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

@Validated
@Data
public class EmailProperties {

    @URL
    private String confirmLink;

    @Email
    private String from;

}
