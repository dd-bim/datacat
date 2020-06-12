package de.bentrm.datacat.graphql.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

public class MeasureInput extends RootInput {

    private String unit;

    @NotNull
    private ItemListInput valueDomain;

    public String getUnit() {
        return unit;
    }

    public ItemListInput getValueDomain() {
        return valueDomain;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("unit", unit)
                .append("valueDomain", valueDomain)
                .toString();
    }
}
