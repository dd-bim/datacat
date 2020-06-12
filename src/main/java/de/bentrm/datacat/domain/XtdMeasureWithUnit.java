package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.SortedSet;
import java.util.TreeSet;

@NodeEntity(label = XtdMeasureWithUnit.LABEL)
public class XtdMeasureWithUnit extends XtdObject {

    public static final String TITLE = "Measure";
    public static final String TITLE_PLURAL = "Measures";
    public static final String LABEL = PREFIX + "MeasureWithUnit";

    @Relationship(type = "HAS_UNIT_COMPONENT")
    private XtdUnit unitComponent;

    @Relationship(type = "HAS_VALUE_DOMAIN")
    private final SortedSet<DomainValueRelationship> valueDomain = new TreeSet<>();

    public XtdUnit getUnitComponent() {
        return unitComponent;
    }

    public void setUnitComponent(XtdUnit unitComponent) {
        this.unitComponent = unitComponent;
    }

    public SortedSet<DomainValueRelationship> getValueDomain() {
        return valueDomain;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("unitComponent", unitComponent)
                .append("valueDomain", valueDomain)
                .toString();
    }
}
