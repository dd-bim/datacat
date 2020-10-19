package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.ValueRole;
import de.bentrm.datacat.catalog.domain.ValueType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SetNominalValueInput {
    @NotBlank String id;
    @NotNull ValueRole valueRole;
    @NotNull ValueType valueType;
    @NotBlank String nominalValue;
}
