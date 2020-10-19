package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdRelGroups;

public interface GroupsService extends QueryService<XtdRelGroups>, OneToManyRelationshipService<XtdRelGroups> {
}
