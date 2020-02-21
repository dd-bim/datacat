package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdClassification.LABEL)
public class XtdClassification extends XtdObject {

    public static final String TITLE = "Classification";
    public static final String TITLE_PLURAL = "Classifications";
    public static final String LABEL = PREFIX + TITLE;

}
