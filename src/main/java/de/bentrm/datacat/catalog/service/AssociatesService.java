package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdRelAssociates;

public interface AssociatesService extends QueryService<XtdRelAssociates>, OneToManyRelationshipService<XtdRelAssociates> {
}
