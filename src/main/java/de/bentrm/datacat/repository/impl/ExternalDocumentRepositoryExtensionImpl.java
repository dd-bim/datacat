package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.repository.ExternalDocumentRepositoryExtension;

public class ExternalDocumentRepositoryExtensionImpl
        extends RepositoryExtensionImpl<XtdExternalDocument>
        implements ExternalDocumentRepositoryExtension {
    public ExternalDocumentRepositoryExtensionImpl() {
        super(XtdExternalDocument.class, XtdExternalDocument.LABEL);
    }
}
