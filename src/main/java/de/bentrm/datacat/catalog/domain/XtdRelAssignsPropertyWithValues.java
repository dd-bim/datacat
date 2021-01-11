package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdRelAssignsPropertyWithValues.LABEL)
public class XtdRelAssignsPropertyWithValues extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsPropertyWithValues";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_PROPERTY_WITH_VALUES";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdObject relatingObject;

    @Relationship(type = RELATIONSHIP_TYPE)
    private XtdProperty relatedProperty;

    @Relationship(type = RELATIONSHIP_TYPE)
    private List<XtdValue> relatedValues = new ArrayList<>();
}
