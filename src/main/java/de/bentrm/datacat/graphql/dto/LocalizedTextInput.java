package de.bentrm.datacat.graphql.dto;

import lombok.Data;

@Data
public class LocalizedTextInput {

    String languageTag;
    String text;

}
