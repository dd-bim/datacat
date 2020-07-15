package de.bentrm.datacat.properties;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "datacat")
public class ApplicationProperties {

    @URL
    private String url;

    @NotNull
    private AuthProperties auth;

    @NotEmpty
    private List<UserProperties> users;

    @NotNull
    private EmailProperties mail;

}
