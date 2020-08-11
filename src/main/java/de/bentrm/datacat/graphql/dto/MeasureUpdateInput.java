package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.validation.IdConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MeasureUpdateInput extends RootUpdateInput {

    private String unitComponent;

    private List<@NotNull @IdConstraint String> valueDomain;
}
