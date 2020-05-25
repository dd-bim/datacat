package de.bentrm.datacat.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "datacat.auth")
@Validated
public class AuthProperties {

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

    /**
     * Basic information of auto-generated admin user.
     */
    @NotNull
    private Admin admin;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public static class Admin {

        /**
         * Username used for auto-generated admin.
         */
        @NotBlank
        private String username;

        /**
         * Password issued to auto-generated admin.
         */
        @NotBlank
        private String password;

        private String firstname = "";

        private String lastname = "";

        private String email = "";

        private String organization = "";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }
    }
}
