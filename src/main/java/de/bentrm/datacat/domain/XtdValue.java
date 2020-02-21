package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdValue.LABEL)
public class XtdValue extends XtdObject {

    public static final String TITLE = "Value";
    public static final String TITLE_PLURAL = "Values";
    public static final String LABEL = PREFIX + TITLE;

    private String lowerTolerance;

    private String upperTolerance;

    private XtdValueRoleEnum valueRole;

    private String nominalValue;

    private XtdToleranceTypeEnum toleranceValue;

    private XtdValueTypeEnum valueType;

    public String getLowerTolerance() {
        return lowerTolerance;
    }

    public XtdValue setLowerTolerance(String lowerTolerance) {
        this.lowerTolerance = lowerTolerance;
        return this;
    }

    public String getUpperTolerance() {
        return upperTolerance;
    }

    public XtdValue setUpperTolerance(String upperTolerance) {
        this.upperTolerance = upperTolerance;
        return this;
    }

    public XtdValueRoleEnum getValueRole() {
        return valueRole;
    }

    public XtdValue setValueRole(XtdValueRoleEnum valueRole) {
        this.valueRole = valueRole;
        return this;
    }

    public String getNominalValue() {
        return nominalValue;
    }

    public XtdValue setNominalValue(String nominalValue) {
        this.nominalValue = nominalValue;
        return this;
    }

    public XtdToleranceTypeEnum getToleranceValue() {
        return toleranceValue;
    }

    public XtdValue setToleranceValue(XtdToleranceTypeEnum toleranceValue) {
        this.toleranceValue = toleranceValue;
        return this;
    }

    public XtdValueTypeEnum getValueType() {
        return valueType;
    }

    public XtdValue setValueType(XtdValueTypeEnum valueType) {
        this.valueType = valueType;
        return this;
    }
}
