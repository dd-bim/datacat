package de.bentrm.datacat.graphql.dto;

import lombok.Data;

@Data
public class LoginInput {

    private String username;
    private String password;
}
