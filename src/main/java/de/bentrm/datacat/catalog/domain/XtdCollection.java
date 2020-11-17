package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdCollection.LABEL)
public abstract class XtdCollection extends XtdRoot {

    public static final String LABEL = "XtdCollection";

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
