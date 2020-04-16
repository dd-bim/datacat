package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;

public interface RelAssociatesService extends CrudEntityService<XtdRelGroups, AssociationInput, AssociationUpdateInput>, AssociationService<XtdRelAssociates> {

}
