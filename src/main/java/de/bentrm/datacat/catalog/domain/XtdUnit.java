package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdUnit.LABEL)
public class XtdUnit extends XtdObject {

    public static final String TITLE = "Unit";
    public static final String TITLE_PLURAL = "Units";
    public static final String LABEL = PREFIX + TITLE;

}
