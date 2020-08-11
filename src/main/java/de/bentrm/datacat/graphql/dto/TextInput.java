package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;
import de.bentrm.datacat.validation.LanguageCodeConstraint;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TextInput {

    @IdConstraint
    private String id;

    @LanguageCodeConstraint
    private String languageCode;

    @NotBlank
    private String value;
}
