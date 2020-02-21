package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelAssignsProperties.LABEL)
public class XtdRelAssignsProperties extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsProperties";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_PROPERTY";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdObject relatingObject;

    @Relationship(type = RELATIONSHIP_TYPE)
    private Set<XtdProperty> relatedProperties = new HashSet<>();

    public XtdObject getRelatingObject() {
        return relatingObject;
    }

    public XtdRelAssignsProperties setRelatingObject(XtdObject relatingObject) {
        this.relatingObject = relatingObject;
        return this;
    }

    public Set<XtdProperty> getRelatedProperties() {
        return relatedProperties;
    }

    public XtdRelAssignsProperties setRelatedProperties(Set<XtdProperty> relatedProperties) {
        this.relatedProperties = relatedProperties;
        return this;
    }
}
