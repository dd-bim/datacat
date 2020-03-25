package de.bentrm.datacat.repository.object.impl;

import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.repository.impl.RootRepositoryExtensionImpl;
import de.bentrm.datacat.repository.object.SubjectRepositoryExtension;

public class SubjectRepositoryExtensionImpl
        extends RootRepositoryExtensionImpl<XtdSubject>
        implements SubjectRepositoryExtension {
    protected SubjectRepositoryExtensionImpl() {
        super(XtdSubject.class, XtdSubject.LABEL);
    }
}
