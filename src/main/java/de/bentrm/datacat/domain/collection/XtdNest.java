package de.bentrm.datacat.domain.collection;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdNest.LABEL)
public class XtdNest extends XtdCollection {

    public static final String TITLE = "Nest";
    public static final String TITLE_PLURAL = "Nests";
    public static final String LABEL = PREFIX + TITLE;

}
