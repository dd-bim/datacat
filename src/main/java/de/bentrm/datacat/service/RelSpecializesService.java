package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelSpecializes;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;

public interface RelSpecializesService
        extends CrudEntityService<XtdRelSpecializes, AssociationInput, AssociationUpdateInput>, AssociationService<XtdRelSpecializes> {

}
