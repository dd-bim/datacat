package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.collection.XtdCollection;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelAssignsCollections.LABEL)
public class XtdRelAssignsCollections extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsCollections";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_COLLECTIONS";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdObject relatingObject;

    @Relationship(type = RELATIONSHIP_TYPE)
    private final Set<XtdCollection> relatedCollections = new HashSet<>();

    public XtdObject getRelatingObject() {
        return relatingObject;
    }

    public void setRelatingObject(XtdObject relatingObject) {
        this.relatingObject = relatingObject;
    }

    public Set<XtdCollection> getRelatedCollections() {
        return relatedCollections;
    }
}
