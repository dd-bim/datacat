package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdValue.LABEL)
public class XtdValue extends XtdObject {

    public static final String TITLE = "Value";
    public static final String TITLE_PLURAL = "Values";
    public static final String LABEL = PREFIX + TITLE;

    private XtdToleranceTypeEnum toleranceType = XtdToleranceTypeEnum.Nil;

    private String lowerTolerance;

    private String upperTolerance;

    private XtdValueRoleEnum valueRole = XtdValueRoleEnum.Nil;

    private XtdValueTypeEnum valueType = XtdValueTypeEnum.Nil;

    private String nominalValue;

    public String getLowerTolerance() {
        return lowerTolerance;
    }

    public void setLowerTolerance(String lowerTolerance) {
        this.lowerTolerance = lowerTolerance;
    }

    public String getUpperTolerance() {
        return upperTolerance;
    }

    public void setUpperTolerance(String upperTolerance) {
        this.upperTolerance = upperTolerance;
    }

    public XtdValueRoleEnum getValueRole() {
        return valueRole;
    }

    public void setValueRole(XtdValueRoleEnum valueRole) {
        this.valueRole = valueRole;
    }

    public String getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(String nominalValue) {
        this.nominalValue = nominalValue;
    }

    public XtdToleranceTypeEnum getToleranceType() {
        return toleranceType;
    }

    public void setToleranceType(XtdToleranceTypeEnum toleranceType) {
        this.toleranceType = toleranceType;
    }

    public XtdValueTypeEnum getValueType() {
        return valueType;
    }

    public void setValueType(XtdValueTypeEnum valueType) {
        this.valueType = valueType;
    }
}
