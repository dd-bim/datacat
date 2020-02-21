package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdProperty.LABEL)
public class XtdProperty extends XtdObject {

    public static final String TITLE = "Property";
    public static final String TITLE_PLURAL = "Properties";
    public static final String LABEL = PREFIX + TITLE;

}
