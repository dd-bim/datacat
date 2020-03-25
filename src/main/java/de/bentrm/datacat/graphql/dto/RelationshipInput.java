package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class RelationshipInput extends RootInput {

    @NotBlank
    @IdConstraint
    String relating;

    @NotEmpty
    List<@NotBlank @IdConstraint String> related = new ArrayList<>();

    public String getRelating() {
        return relating;
    }

    public List<String> getRelated() {
        return related;
    }
}
