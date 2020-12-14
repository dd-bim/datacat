package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.graphql.OneToOneRelationship;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateOneToOneRelationshipInput {
    @NotNull OneToOneRelationship relationshipType;
    @Valid CatalogEntryPropertiesInput properties;
    @NotBlank String from;
    @NotBlank String to;
}
