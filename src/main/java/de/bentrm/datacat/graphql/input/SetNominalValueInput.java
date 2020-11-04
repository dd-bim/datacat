package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SetNominalValueInput {
    @NotBlank String id;
    @NotNull @Valid NominalValueInput nominalValue;
}
