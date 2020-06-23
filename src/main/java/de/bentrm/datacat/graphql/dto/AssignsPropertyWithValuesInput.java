package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AssignsPropertyWithValuesInput extends RootInput {

    @NotBlank @IdConstraint
    private String relatingObject;

    @NotBlank @IdConstraint
    private String relatedProperty;

    private @NotNull
    final List<@NotBlank @IdConstraint String> relatedValues = new ArrayList<>();

    public String getRelatingObject() {
        return relatingObject;
    }

    public String getRelatedProperty() {
        return relatedProperty;
    }

    public List<String> getRelatedValues() {
        return relatedValues;
    }
}
