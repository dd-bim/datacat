package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdProperty;
import de.bentrm.datacat.domain.XtdValue;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity(label = XtdRelAssignsPropertyWithValues.LABEL)
public class XtdRelAssignsPropertyWithValues extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsPropertyWithValues";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_PROPERTY_WITH_VALUES";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdObject relatingObject;

    @Relationship(type = RELATIONSHIP_TYPE)
    private XtdProperty relatedProperty;

    @Relationship(type = RELATIONSHIP_TYPE)
    private final List<XtdValue> relatedValues = new ArrayList<>();

    public XtdObject getRelatingObject() {
        return relatingObject;
    }

    public XtdRelAssignsPropertyWithValues setRelatingObject(XtdObject relatingObject) {
        this.relatingObject = relatingObject;
        return this;
    }

    public XtdProperty getRelatedProperty() {
        return relatedProperty;
    }

    public XtdRelAssignsPropertyWithValues setRelatedProperty(XtdProperty relatedProperty) {
        this.relatedProperty = relatedProperty;
        return this;
    }

    public List<XtdValue> getRelatedValues() {
        return relatedValues;
    }
}
