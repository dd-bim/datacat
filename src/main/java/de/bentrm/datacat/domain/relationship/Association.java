package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdRoot;

import java.util.Set;

public interface Association {

    XtdRoot getRelatingThing();

    void setRelatingThing(XtdRoot relatingThing);

    Set<XtdRoot> getRelatedThings();

    void setRelatedThings(Set<XtdRoot> relatedThings);
}
