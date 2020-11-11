package de.bentrm.datacat.catalog.domain;

import lombok.Getter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NodeEntity(label = XtdCollection.LABEL)
public abstract class XtdCollection extends XtdRoot {

    public static final String TITLE = "Collection";
    public static final String TITLE_PLURAL = "Collections";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE)
    private final Set<XtdRelCollects> collects = new HashSet<>();

    @Relationship(type = XtdRelAssignsCollections.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelAssignsCollections> assignedTo = new HashSet<>();

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return Stream.of(super.getOwnedRelationships(), collects)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
