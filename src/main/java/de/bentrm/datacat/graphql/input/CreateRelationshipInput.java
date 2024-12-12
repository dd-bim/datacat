package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateRelationshipInput {
    @NotNull SimpleRelationType relationshipType;
    @Valid RelationshipPropertiesInput properties;
    @NotBlank String fromId;
    @NotEmpty List<String> toIds;
}
