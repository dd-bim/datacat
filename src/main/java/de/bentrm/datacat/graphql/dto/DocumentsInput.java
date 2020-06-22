package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class DocumentsInput extends RootInput {

    @NotBlank @IdConstraint
    private String relatingDocument;

    private @NotNull
    final List<@NotBlank @IdConstraint String> relatedThings = new ArrayList<>();

    public String getRelatingDocument() {
        return relatingDocument;
    }

    public List<String> getRelatedThings() {
        return relatedThings;
    }
}
