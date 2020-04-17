package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;

public interface RelAssociatesService
        extends CrudEntityService<XtdRelAssociates, AssociationInput, AssociationUpdateInput>,
        AssociationService<XtdRelAssociates> {

}
