package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdRelClassifies;

public interface ClassifiesRelationshipService extends QueryService<XtdRelClassifies>, OneToManyRelationshipService<XtdRelClassifies> {
}
