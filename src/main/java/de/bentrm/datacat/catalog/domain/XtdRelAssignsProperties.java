package de.bentrm.datacat.catalog.domain;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NodeEntity(label = XtdRelAssignsProperties.LABEL)
public class XtdRelAssignsProperties extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsProperties";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_PROPERTY";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdObject relatingObject;

    @Relationship(type = RELATIONSHIP_TYPE)
    private final Set<XtdProperty> relatedProperties = new HashSet<>();
}
