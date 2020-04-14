package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdMeasureWithUnit.LABEL)
public class XtdMeasureWithUnit extends XtdObject {

    public static final String TITLE = "Measure";
    public static final String TITLE_PLURAL = "Measures";
    public static final String LABEL = PREFIX + "MeasureWithUnit";

}
