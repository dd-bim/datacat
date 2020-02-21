package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdObject;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelAssociates.LABEL)
public class XtdRelAssociates extends XtdRelationship {

    public static final String LABEL = "XtdRelAssociates";
    public static final String RELATIONSHIP_TYPE = "ASSOCIATES";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdObject relatingObject;

    @Relationship(type = RELATIONSHIP_TYPE)
    private Set<XtdObject> relatedObjects = new HashSet<>();

    public XtdObject getRelatingObject() {
        return relatingObject;
    }

    public void setRelatingObject(XtdObject relatingObject) {
        this.relatingObject = relatingObject;
    }

    public Set<XtdObject> getRelatedObjects() {
        return relatedObjects;
    }

    public void setRelatedObjects(Set<XtdObject> relatedObjects) {
        this.relatedObjects = relatedObjects;
    }
}
