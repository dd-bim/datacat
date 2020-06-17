package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AssignsCollectionsUpdateInput extends RootUpdateInput {

    @NotBlank @IdConstraint
    private String relatingObject;

    private @NotNull
    final List<@NotBlank @IdConstraint String> relatedCollections = new ArrayList<>();

    public String getRelatingObject() {
        return relatingObject;
    }

    public List<String> getRelatedCollections() {
        return relatedCollections;
    }
}
