package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

public class IdInput {

    private int index;

    @NotNull
    @IdConstraint
    private String id;

    public int getIndex() {
        return index;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("index", index)
                .append("id", id)
                .toString();
    }
}
