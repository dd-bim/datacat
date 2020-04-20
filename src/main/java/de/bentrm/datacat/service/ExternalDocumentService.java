package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.graphql.dto.EntityInput;
import de.bentrm.datacat.graphql.dto.EntityUpdateInput;

public interface ExternalDocumentService extends CrudEntityService<XtdExternalDocument, EntityInput, EntityUpdateInput> {
}
