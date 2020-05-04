package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelActsUpon;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;

public interface RelActsUponService extends CrudEntityService<XtdRelActsUpon, AssociationInput, AssociationUpdateInput>, AssociationService<XtdRelActsUpon> {

}
