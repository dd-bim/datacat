package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AssociationInput extends RootInput {

    @NotBlank @IdConstraint
    private String relatingThing;

    private @NotNull List<@NotBlank @IdConstraint String> relatedThings = new ArrayList<>();

    public String getRelatingThing() {
        return relatingThing;
    }

    public List<String> getRelatedThings() {
        return relatedThings;
    }
}
