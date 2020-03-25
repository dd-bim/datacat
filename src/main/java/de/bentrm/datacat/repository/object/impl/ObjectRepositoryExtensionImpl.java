package de.bentrm.datacat.repository.object.impl;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.repository.impl.RootRepositoryExtensionImpl;
import de.bentrm.datacat.repository.object.ObjectRepositoryExtension;

public class ObjectRepositoryExtensionImpl
        extends RootRepositoryExtensionImpl<XtdObject>
        implements ObjectRepositoryExtension {
    protected ObjectRepositoryExtensionImpl() {
        super(XtdObject.class, XtdObject.LABEL);
    }
}
