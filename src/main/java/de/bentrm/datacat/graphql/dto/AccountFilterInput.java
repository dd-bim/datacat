package de.bentrm.datacat.graphql.dto;

import lombok.Data;

@Data
public class AccountFilterInput {
    private String query;
    private Boolean expired;
    private Boolean locked;
    private Boolean credentialsExpired;
    private Boolean enabled;
    private Integer pageSize;
    private Integer pageNumber;
}
