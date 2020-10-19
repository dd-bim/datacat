package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdActivity.LABEL)
public class XtdActivity extends XtdObject {

    public static final String TITLE = "Activity";
    public static final String TITLE_PLURAL = "Activities";
    public static final String LABEL = PREFIX + TITLE;

}
