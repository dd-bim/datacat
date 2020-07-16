package de.bentrm.datacat.properties;

import de.bentrm.datacat.domain.Role;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Validated
@ConfigurationProperties(prefix = "datacat")
public class AppProperties {

    @URL
    private String url;

    @NotNull
    private AuthProperties auth;

    @NotEmpty
    private Map<String, UserProperties> users = new HashMap<>();

    @NotNull
    private EmailProperties mail;

    @Validated
    @Data
    public static class AuthProperties {

        /**
         * Secret used to verify and sign JWT tokens.
         */
        @NotBlank
        private String secret;

        /**
         * Issuer of JWT tokens.
         */
        @NotBlank
        private String issuer;
    }

    @Validated
    @Data
    public static class UserProperties {

        @NotBlank
        private String firstName;

        @NotBlank
        private String lastName;

        @NotBlank
        private String email;

        @NotNull
        private String organization = "";

        private String password;

        @NotEmpty
        private List<@NotNull Role> roles = new ArrayList<>();
    }

    @Validated
    @Data
    public static class EmailProperties {

        @URL
        private String confirmLink;

        @Email
        private String from;

    }
}
