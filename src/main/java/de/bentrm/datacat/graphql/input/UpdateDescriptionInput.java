package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class UpdateDescriptionInput {
    @NotNull String descriptionId;
    @NotNull String value;
}
