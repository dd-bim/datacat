package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.graphql.QualifiedOneToOneRelationshipType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateQualifiedOneToManyRelationshipInput {
    @NotNull QualifiedOneToOneRelationshipType relationshipType;
    @Valid CatalogEntryPropertiesInput properties;
    @NotBlank String from;
    @NotBlank String to;
    @NotEmpty List<@NotBlank String> with;
}
