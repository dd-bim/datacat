package de.bentrm.datacat.graphql.dto;

import lombok.Data;

@Data
public class LocalizedTextDto {

    private String languageTag;
    private String text;

}
