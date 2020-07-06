package de.bentrm.datacat.graphql.dto;

import lombok.Data;

@Data
public class ProfileUpdateInput {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String organization;
}
