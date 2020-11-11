package de.bentrm.datacat.catalog.domain;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;

@Getter
@Setter
@NodeEntity(label = XtdValue.LABEL)
public class XtdValue extends XtdObject {

    public static final String TITLE = "Value";
    public static final String TITLE_PLURAL = "Values";
    public static final String LABEL = PREFIX + TITLE;

    private ToleranceType toleranceType;

    private String lowerTolerance;

    private String upperTolerance;

    private ValueRole valueRole;

    private ValueType valueType;

    private String nominalValue;
}
