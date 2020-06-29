package de.bentrm.datacat.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

public class TranslationInput {

    @NotBlank
    private String languageCode;

    @NotBlank
    private String value;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("languageCode", languageCode)
                .append("value", value)
                .toString();
    }
}
