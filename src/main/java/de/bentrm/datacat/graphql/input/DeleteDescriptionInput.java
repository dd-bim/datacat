package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteDescriptionInput {
    @NotNull String descriptionId;
}
