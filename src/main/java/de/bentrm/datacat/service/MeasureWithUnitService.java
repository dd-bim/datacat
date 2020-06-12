package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.graphql.dto.MeasureInput;
import de.bentrm.datacat.graphql.dto.MeasureUpdateInput;

public interface MeasureWithUnitService extends CrudEntityService<XtdMeasureWithUnit, MeasureInput, MeasureUpdateInput> {

}
