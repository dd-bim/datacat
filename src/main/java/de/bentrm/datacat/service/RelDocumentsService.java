package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.graphql.dto.RelDocumentsInput;
import de.bentrm.datacat.graphql.dto.RelDocumentsUpdateInput;

public interface RelDocumentsService extends CrudEntityService<XtdRelDocuments, RelDocumentsInput, RelDocumentsUpdateInput> {

}
