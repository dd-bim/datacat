package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

public interface MeasureWithUnitService extends CrudEntityService<XtdMeasureWithUnit, RootInput, RootUpdateInput> {

}
