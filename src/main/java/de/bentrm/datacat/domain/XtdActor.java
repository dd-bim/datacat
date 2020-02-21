package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdActor.LABEL)
public class XtdActor extends XtdObject {

    public static final String TITLE = "Actor";
    public static final String TITLE_PLURAL = "Actors";
    public static final String LABEL = PREFIX + TITLE;

}
