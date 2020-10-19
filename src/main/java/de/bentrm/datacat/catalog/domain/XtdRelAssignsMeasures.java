package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelAssignsMeasures.LABEL)
public class XtdRelAssignsMeasures extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsMeasures";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_MEASURE";

    private final Set<Translation> methodOfInterpretation = new HashSet<>();

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdProperty relatingProperty;

    @Relationship(type = RELATIONSHIP_TYPE)
    private final Set<XtdMeasureWithUnit> relatedMeasures = new HashSet<>();

    public Set<Translation> getMethodOfInterpretation() {
        return methodOfInterpretation;
    }

    public XtdProperty getRelatingProperty() {
        return relatingProperty;
    }

    public void setRelatingProperty(XtdProperty relatingProperty) {
        this.relatingProperty = relatingProperty;
    }

    public Set<XtdMeasureWithUnit> getRelatedMeasures() {
        return relatedMeasures;
    }
}
