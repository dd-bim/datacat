package de.bentrm.datacat.service.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class AccountUpdateDto {
    @NotBlank String username;
    @NotBlank String firstName;
    @NotBlank String lastName;
    @Email String email;
    @NotNull String organization;
}
