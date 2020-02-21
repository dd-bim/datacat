package de.bentrm.datacat.domain.collection;

import de.bentrm.datacat.domain.XtdRoot;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdCollection.LABEL)
public abstract class XtdCollection extends XtdRoot {

    public static final String TITLE = "Collection";
    public static final String TITLE_PLURAL = "Collections";
    public static final String LABEL = PREFIX + TITLE;

}
