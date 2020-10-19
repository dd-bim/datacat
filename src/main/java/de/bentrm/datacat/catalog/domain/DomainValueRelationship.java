package de.bentrm.datacat.catalog.domain;

import org.jetbrains.annotations.NotNull;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.id.UuidStrategy;

@RelationshipEntity("HAS_VALUE_DOMAIN")
public class DomainValueRelationship implements Comparable<DomainValueRelationship> {

    @Id
    @GeneratedValue(strategy = UuidStrategy.class)
    protected String id;

    @StartNode
    private XtdMeasureWithUnit measure;

    @EndNode
    private XtdValue value;

    @Property
    private int sortOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public XtdMeasureWithUnit getMeasure() {
        return measure;
    }

    public void setMeasure(XtdMeasureWithUnit measure) {
        this.measure = measure;
    }

    public XtdValue getValue() {
        return value;
    }

    public void setValue(XtdValue value) {
        this.value = value;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public int compareTo(@NotNull DomainValueRelationship o) {
        return Integer.compare(sortOrder, o.sortOrder);
    }
}
