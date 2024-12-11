package de.bentrm.datacat.properties;

import de.bentrm.datacat.auth.domain.Role;
import de.bentrm.datacat.properties.AppProperties.AuthProperties;
import de.bentrm.datacat.properties.AppProperties.EmailProperties;
import de.bentrm.datacat.properties.AppProperties.UserProperties;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Validated
@ConfigurationProperties(prefix = "datacat")
public class AppProperties {

    @NotNull
    private AppProperties.ClientProperties client;

    @NotNull
    private AuthProperties auth;

    @NotEmpty
    private Map<String, UserProperties> users = new HashMap<>();

    @NotNull
    private EmailProperties mail;

    /**
     * Properties that describe the client application that interacts with the API.
     */
    @Validated
    @Data
    public static class ClientProperties {
        /**
         * URL that the user is redirected to after successful validation of her email address.
         */
        @URL
        private String url;
    }

    /**
     * Authorization properties that are used to generate session / JWT tokens.
     */
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

    /**
     * Initial user settings that are applied at first boot.
     */
    @Validated
    @Data
    public static class UserProperties {

        /**
         * First name of the user.
         */
        @NotBlank
        private String firstName;

        /**
         * Last name of the user.
         */
        @NotBlank
        private String lastName;

        /**
         * Email address of the user.
         */
        @NotBlank
        private String email;

        /**
         * Organization that the user is registering for.
         */
        @NotNull
        private String organization = "";

        /**
         * User password.
         */
        private String password;

        /**
         * Roles of the user.
         */
        @NotEmpty
        private List<@NotNull Role> roles = new ArrayList<>();
    }

    /**
     * Notification settings. Mail server settings are are set via @{@link org.springframework.boot.autoconfigure.mail.MailSenderPropertiesConfiguration}.
     */
    @Validated
    @Data
    public static class EmailProperties {

        /**
         * Sender email for notification messages.
         */
        @Email
        private String from;

    }
}
