package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RelDocumentsInput extends RootInput {

    @NotBlank @IdConstraint
    private String relatingDocument;

    private @NotNull
    final List<@NotBlank @IdConstraint String> relatedObjects = new ArrayList<>();

    public String getRelatingDocument() {
        return relatingDocument;
    }

    public List<String> getRelatedObjects() {
        return relatedObjects;
    }
}
