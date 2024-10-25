package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.Enums.XtdUnitBaseEnum;
import de.bentrm.datacat.catalog.domain.Enums.XtdUnitScaleEnum;
import lombok.Data;

@Data
public class UnitInput {
    XtdUnitScaleEnum scale;
    XtdUnitBaseEnum base;
}
