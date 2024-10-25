package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import de.bentrm.datacat.catalog.domain.Enums.XtdPropertyRelationshipTypeEnum;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdRelationshipToProperty.LABEL)
public class XtdRelationshipToProperty extends AbstractRelationship {

    public static final String LABEL = "XtdRelationshipToProperty";
    public static final String RELATIONSHIP_TYPE = "CONNECTED_PROPERTIES";
    public static final String RELATIONSHIP_TYPE_OUT = "TARGET_PROPERTIES";

    @ToString.Include
    private XtdPropertyRelationshipTypeEnum relationshipType;

    @ToString.Include
    @Relationship(type = XtdRelationshipToProperty.RELATIONSHIP_TYPE_OUT)
    private final Set<XtdProperty> targetProperties = new HashSet<>();

    @ToString.Include
    @Relationship(type = XtdRelationshipToProperty.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdProperty connectingProperty;

}
