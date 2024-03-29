package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IdInput {

    @NotNull
    @IdConstraint
    private String id;

    private int index;
}
