package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.graphql.OneToManyRelationshipType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateOneToManyRelationshipInput {
    @NotNull OneToManyRelationshipType relationshipType;
    @Valid EntryPropertiesInput properties;
    @NotBlank String from;
    @NotEmpty List<@NotBlank String> to;
}
