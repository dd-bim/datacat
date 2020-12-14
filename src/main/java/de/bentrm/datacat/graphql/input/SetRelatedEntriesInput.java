package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SetRelatedEntriesInput {
    @NotBlank String relationshipId;
    @NotEmpty List<String> toIds;
}
