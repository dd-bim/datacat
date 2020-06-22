package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.graphql.dto.DocumentsInput;
import de.bentrm.datacat.graphql.dto.DocumentsUpdateInput;

public interface RelDocumentsService extends CrudEntityService<XtdRelDocuments, DocumentsInput, DocumentsUpdateInput> {

}
