package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.ArrayList;
import java.util.List;

@NodeEntity(label = XtdMeasureWithUnit.LABEL)
public class XtdMeasureWithUnit extends XtdObject {

    public static final String TITLE = "Measure";
    public static final String TITLE_PLURAL = "Measures";
    public static final String LABEL = PREFIX + "MeasureWithUnit";

    private XtdUnit unitComponent;

    private List<XtdValue> valueDomain = new ArrayList<>();

    public XtdUnit getUnitComponent() {
        return unitComponent;
    }

    public XtdMeasureWithUnit setUnitComponent(XtdUnit unitComponent) {
        this.unitComponent = unitComponent;
        return this;
    }

    public List<XtdValue> getValueDomain() {
        return valueDomain;
    }

    public XtdMeasureWithUnit setValueDomain(List<XtdValue> valueDomain) {
        this.valueDomain = valueDomain;
        return this;
    }
}
