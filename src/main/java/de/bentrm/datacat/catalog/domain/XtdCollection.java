package de.bentrm.datacat.catalog.domain;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NodeEntity(label = XtdCollection.LABEL)
public abstract class XtdCollection extends XtdRoot {

    public static final String TITLE = "Collection";
    public static final String TITLE_PLURAL = "Collections";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE)
    private final Set<XtdRelCollects> collects = new HashSet<>();

    @Relationship(type = XtdRelAssignsCollections.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelAssignsCollections> assignedTo = new HashSet<>();
}
