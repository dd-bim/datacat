package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdRelationshipToProperty.LABEL)
public abstract class XtdRelationshipToProperty extends XtdConcept {

    public static final String LABEL = "XtdRelationshipToProperty";
    public static final String RELATIONSHIP_TYPE = "CONNECTED_PROPERTIES";

    @ToString.Include
    private XtdPropertyRelationshipTypeEnum propertyRelationshipTypeEnum;

    @ToString.Include
    @Relationship(type = "RELATED_PROPERTIES")
    private final Set<XtdSubject> targetProperties = new HashSet<>();

}
