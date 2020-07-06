package de.bentrm.datacat.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProfileDto {
    String username;
    String firstName;
    String lastName;
    String email;
    String organization;
}
