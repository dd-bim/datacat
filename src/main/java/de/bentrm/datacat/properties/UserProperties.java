package de.bentrm.datacat.properties;

import de.bentrm.datacat.domain.Role;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Data
public class UserProperties {

    @NotBlank
    private String username;

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
    private List<@NotNull Role> roles;
}
