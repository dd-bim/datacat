package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MeasureInput extends RootInput {

    private String unitComponent;

    private List<@NotNull @IdConstraint String> valueDomain;

    public String getUnitComponent() {
        return unitComponent;
    }

    public List<String> getValueDomain() {
        return valueDomain;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("unitComponent", unitComponent)
                .append("valueDomain", valueDomain)
                .toString();
    }
}
