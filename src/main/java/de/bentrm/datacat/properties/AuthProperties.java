package de.bentrm.datacat.properties;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Data
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
}
