package de.bentrm.datacat.catalog.domain;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NodeEntity(label = XtdRoot.LABEL)
public abstract class XtdRoot extends CatalogItem {

    public static final String TITLE = "Root";
    public static final String TITLE_PLURAL = "Roots";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelCollects> collectedBy = new HashSet<>();

    @Relationship(type = XtdRelAssociates.RELATIONSHIP_TYPE)
    private final Set<XtdRelAssociates> associates = new HashSet<>();

    @Relationship(type = XtdRelAssociates.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelAssociates> associatedBy = new HashSet<>();

    @Relationship(type = XtdRelDocuments.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelDocuments> documentedBy = new HashSet<>();

    @Relationship(type = XtdRelGroups.RELATIONSHIP_TYPE)
    private final Set<XtdRelGroups> groups = new HashSet<>();

    @Relationship(type = XtdRelGroups.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelGroups> groupedBy = new HashSet<>();

    @Relationship(type = XtdRelSpecializes.RELATIONSHIP_TYPE)
    private final Set<XtdRelSpecializes> specializes = new HashSet<>();

    @Relationship(type = XtdRelSpecializes.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelSpecializes> specializedBy = new HashSet<>();

    @Relationship(type = XtdRelComposes.RELATIONSHIP_TYPE)
    private final Set<XtdRelComposes> composes = new HashSet<>();

    @Relationship(type = XtdRelComposes.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelComposes> composedBy = new HashSet<>();

    @Relationship(type = XtdRelActsUpon.RELATIONSHIP_TYPE)
    private final Set<XtdRelActsUpon> actsUpon = new HashSet<>();

    @Relationship(type = XtdRelActsUpon.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelActsUpon> actedUponBy = new HashSet<>();
}
