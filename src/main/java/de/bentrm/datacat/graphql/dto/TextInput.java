package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;
import de.bentrm.datacat.validation.LanguageCodeConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

public class TextInput {

    @IdConstraint
    private String id;

    @LanguageCodeConstraint
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("languageCode", languageCode)
                .append("value", value)
                .toString();
    }
}
