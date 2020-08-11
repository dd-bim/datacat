package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class EntityUpdateInput {

    @NotBlank @IdConstraint
    private String id;

    private @NotEmpty
    final List<@Valid @NotNull TextInput> names = new ArrayList<>();
}
