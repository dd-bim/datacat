package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.domain.XtdValue;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity(label = XtdRelAssignsValues.LABEL)
public class XtdRelAssignsValues extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsValues";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_VALUE";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdMeasureWithUnit relatingMeasure;

    @Relationship(type = RELATIONSHIP_TYPE)
    private final List<XtdValue> relatedValues = new ArrayList<>();

    public XtdMeasureWithUnit getRelatingMeasure() {
        return relatingMeasure;
    }

    public XtdRelAssignsValues setRelatingMeasure(XtdMeasureWithUnit relatingMeasure) {
        this.relatingMeasure = relatingMeasure;
        return this;
    }

    public List<XtdValue> getRelatedValues() {
        return relatedValues;
    }
}
