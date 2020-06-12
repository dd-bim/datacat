package de.bentrm.datacat.graphql.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

public class MeasureUpdateInput extends RootUpdateInput {

    private String unit;

    @NotNull
    private ItemListUpdateInput valueDomain;

    public String getUnit() {
        return unit;
    }

    public ItemListUpdateInput getValueDomain() {
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
