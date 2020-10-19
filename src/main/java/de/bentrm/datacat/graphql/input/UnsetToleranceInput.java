package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UnsetToleranceInput {
    @NotBlank String id;
}
