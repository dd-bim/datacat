package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdRelDocuments;

public interface DocumentsService extends QueryService<XtdRelDocuments>, OneToManyRelationshipService<XtdRelDocuments> {
}
