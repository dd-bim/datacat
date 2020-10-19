package de.bentrm.datacat.catalog.domain;

import java.util.Set;

public interface Association {

    XtdRoot getRelatingThing();

    void setRelatingThing(XtdRoot relatingThing);

    Set<XtdRoot> getRelatedThings();
}
