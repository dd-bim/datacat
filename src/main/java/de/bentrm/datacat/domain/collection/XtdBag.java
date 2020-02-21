package de.bentrm.datacat.domain.collection;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdBag.LABEL)
public class XtdBag extends XtdCollection {

    public static final String TITLE = "Bag";
    public static final String TITLE_PLURAL = "Bags";
    public static final String LABEL = PREFIX + TITLE;

}
