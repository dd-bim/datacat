package de.bentrm.datacat.dto;

import javax.validation.constraints.NotBlank;

public class TextInputDto {

    private String id;

    @NotBlank
    private String languageCode;

    @NotBlank
    private String value;

    public String getId() {
        return id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getValue() {
        return value;
    }
}
