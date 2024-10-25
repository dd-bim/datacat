package de.bentrm.datacat.graphql.input;

import lombok.Data;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.SimpleRelationType;

@Data
public class DeleteRelationshipInput {
    @NotNull SimpleRelationType relationshipType;
    @NotBlank String fromId;
    @NotBlank String toId;
}
