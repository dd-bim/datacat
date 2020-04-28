package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelComposes;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;

public interface RelComposesService extends CrudEntityService<XtdRelComposes, AssociationInput, AssociationUpdateInput>, AssociationService<XtdRelComposes> {

}
