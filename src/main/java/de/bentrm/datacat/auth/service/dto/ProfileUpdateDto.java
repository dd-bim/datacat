package de.bentrm.datacat.auth.service.dto;

import lombok.Builder;
import lombok.Value;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Value
@Builder
public class ProfileUpdateDto {
    @NotBlank String username;
    @NotBlank String firstName;
    @NotBlank String lastName;
    @Email String email;
    @NotNull String organization;
}
