package de.bentrm.datacat.catalog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@NodeEntity(label = XtdRoot.LABEL)
public abstract class XtdRoot extends CatalogItem {

    public static final String TITLE = "Root";
    public static final String TITLE_PLURAL = "Roots";
    public static final String LABEL = PREFIX + TITLE;

    @ToString.Exclude
    @Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelCollects> collectedBy = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelAssociates.RELATIONSHIP_TYPE)
    private final Set<XtdRelAssociates> associates = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelAssociates.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelAssociates> associatedBy = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelDocuments.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelDocuments> documentedBy = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelGroups.RELATIONSHIP_TYPE)
    private final Set<XtdRelGroups> groups = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelGroups.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelGroups> groupedBy = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelSpecializes.RELATIONSHIP_TYPE)
    private final Set<XtdRelSpecializes> specializes = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelSpecializes.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelSpecializes> specializedBy = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelComposes.RELATIONSHIP_TYPE)
    private final Set<XtdRelComposes> composes = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelComposes.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelComposes> composedBy = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelActsUpon.RELATIONSHIP_TYPE)
    private final Set<XtdRelActsUpon> actsUpon = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = XtdRelActsUpon.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelActsUpon> actedUponBy = new HashSet<>();

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return Stream
                .of(associates, composes, groups, actsUpon)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
