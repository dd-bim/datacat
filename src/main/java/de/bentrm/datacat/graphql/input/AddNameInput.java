package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
public class AddNameInput {
    @NotNull String catalogEntryId;
    @NotNull @Valid TranslationInput name;
}
