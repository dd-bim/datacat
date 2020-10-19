package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class AddDescriptionInput {
    @NotNull String entryId;
    @NotNull @Valid TranslationInput description;
}
