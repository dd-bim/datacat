package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.collection.XtdCollection;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelAssignsCollections.LABEL)
public class XtdRelAssignsCollections extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsCollection";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_COLLECTION";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private Object relatingObject;

    @Relationship(type = RELATIONSHIP_TYPE)
    private Set<XtdCollection> relatedCollections = new HashSet<>();

    public Object getRelatingObject() {
        return relatingObject;
    }

    public XtdRelAssignsCollections setRelatingObject(Object relatingObject) {
        this.relatingObject = relatingObject;
        return this;
    }

    public Set<XtdCollection> getRelatedCollections() {
        return relatedCollections;
    }

    public XtdRelAssignsCollections setRelatedCollections(Set<XtdCollection> relatedCollections) {
        this.relatedCollections = relatedCollections;
        return this;
    }
}
