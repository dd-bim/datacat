package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class DeleteTextInput {
    @NotNull String catalogEntryId;
    @NotNull String textId;
}
