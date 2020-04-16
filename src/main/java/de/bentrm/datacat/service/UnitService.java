package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdUnit;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

public interface UnitService extends CrudEntityService<XtdUnit, RootInput, RootUpdateInput> {
}
