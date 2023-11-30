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
@NodeEntity(label = XtdObject.LABEL)
public abstract class XtdObject extends XtdRoot {

    public static final String LABEL = "XtdObject";

    @Relationship(type = XtdRelAssignsCollections.RELATIONSHIP_TYPE)
    private final Set<XtdRelAssignsCollections> assignedCollections = new HashSet<>();

    @Relationship(type = XtdRelAssignsProperties.RELATIONSHIP_TYPE)
    private final Set<XtdRelAssignsProperties> assignedProperties = new HashSet<>();

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return Stream
                .of(
                    super.getOwnedRelationships(),
                    assignedCollections,
                    assignedProperties
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
