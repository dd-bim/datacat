package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.domain.XtdProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelAssignsMeasures.LABEL)
public class XtdRelAssignsMeasures extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsMeasures";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_MEASURE";

    private XtdName methodOfInterpretation;

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdProperty relatingProperty;

    @Relationship(type = RELATIONSHIP_TYPE)
    private Set<XtdMeasureWithUnit> relatedMeasures = new HashSet<>();

    public XtdName getMethodOfInterpretation() {
        return methodOfInterpretation;
    }

    public XtdRelAssignsMeasures setMethodOfInterpretation(XtdName methodOfInterpretation) {
        this.methodOfInterpretation = methodOfInterpretation;
        return this;
    }

    public XtdProperty getRelatingProperty() {
        return relatingProperty;
    }

    public XtdRelAssignsMeasures setRelatingProperty(XtdProperty relatingProperty) {
        this.relatingProperty = relatingProperty;
        return this;
    }

    public Set<XtdMeasureWithUnit> getRelatedMeasures() {
        return relatedMeasures;
    }

    public XtdRelAssignsMeasures setRelatedMeasures(Set<XtdMeasureWithUnit> relatedMeasures) {
        this.relatedMeasures = relatedMeasures;
        return this;
    }
}
