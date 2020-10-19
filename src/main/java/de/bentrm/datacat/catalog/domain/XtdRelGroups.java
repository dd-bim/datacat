package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelGroups.LABEL)
public class XtdRelGroups extends XtdRelationship implements Association {

    public static final String LABEL = "XtdRelGroups";
    public static final String RELATIONSHIP_TYPE = "GROUPS";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdRoot relatingThing;

    @Relationship(type = RELATIONSHIP_TYPE)
    private final Set<XtdRoot> relatedThings = new HashSet<>();

    public XtdRoot getRelatingThing() {
        return relatingThing;
    }

    public void setRelatingThing(XtdRoot relatingThing) {
        this.relatingThing = relatingThing;
    }

    public Set<XtdRoot> getRelatedThings() {
        return relatedThings;
    }
}
