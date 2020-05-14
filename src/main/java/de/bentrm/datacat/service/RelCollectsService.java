package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import de.bentrm.datacat.graphql.dto.CollectsInput;
import de.bentrm.datacat.graphql.dto.CollectsUpdateInput;

public interface RelCollectsService extends CrudEntityService<XtdRelCollects, CollectsInput, CollectsUpdateInput> {

}
