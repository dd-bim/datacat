package de.bentrm.datacat.graphql.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ItemListUpdateInput extends RootUpdateInput {

    @NotNull
    private List<@NotNull IdInput> insert = new ArrayList<>();

    @NotNull
    private List<@NotNull String> remove = new ArrayList<>();

    public List<IdInput> getInsert() {
        return insert;
    }

    public List<String> getRemove() {
        return remove;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("add", insert)
                .append("remove", remove)
                .toString();
    }
}
