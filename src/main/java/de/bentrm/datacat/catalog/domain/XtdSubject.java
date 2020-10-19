package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdSubject.LABEL)
public class XtdSubject extends XtdObject {

    public static final String TITLE = "Subject";
    public static final String TITLE_PLURAL = "Subjects";
    public static final String LABEL = PREFIX + TITLE;

}
