package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateRelationshipInput {
    @NotNull CatalogRecordType relationshipType;
    @Valid RelationshipPropertiesInput properties;
    @NotBlank String fromId;
    @NotEmpty List<String> toIds;
}
