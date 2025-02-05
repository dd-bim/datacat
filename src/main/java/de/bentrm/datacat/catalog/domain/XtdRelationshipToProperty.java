package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import de.bentrm.datacat.catalog.domain.Enums.XtdPropertyRelationshipTypeEnum;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdRelationshipToProperty.LABEL)
public class XtdRelationshipToProperty extends XtdConcept {

    public static final String LABEL = "XtdRelationshipToProperty";
    public static final String RELATIONSHIP_TYPE = "CONNECTED_PROPERTIES";
    public static final String RELATIONSHIP_TYPE_OUT = "TARGET_PROPERTIES";

    @ToString.Include
    private XtdPropertyRelationshipTypeEnum relationshipType;

    @ToString.Include
    @Relationship(type = XtdRelationshipToProperty.RELATIONSHIP_TYPE_OUT)
    private Set<XtdProperty> targetProperties = new HashSet<>();

    @ToString.Include
    @Relationship(type = XtdRelationshipToProperty.RELATIONSHIP_TYPE, direction = Relationship.Direction.INCOMING)
    private XtdProperty connectingProperty;

}
