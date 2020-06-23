package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.graphql.dto.AssignsPropertyWithValuesInput;
import de.bentrm.datacat.graphql.dto.AssignsPropertyWithValuesUpdateInput;

public interface RelAssignsPropertyWithValuesService extends CrudEntityService<XtdRelAssignsPropertyWithValues, AssignsPropertyWithValuesInput, AssignsPropertyWithValuesUpdateInput> {

}
