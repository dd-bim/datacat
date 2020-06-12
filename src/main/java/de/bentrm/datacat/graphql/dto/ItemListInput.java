package de.bentrm.datacat.graphql.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ItemListInput {

    @NotNull
    private List<@NotNull String> add = new ArrayList<>();

    public List<String> getAdd() {
        return add;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("add", add)
                .toString();
    }
}
