package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdRelAssignsMeasures.LABEL)
public class XtdRelAssignsMeasures extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsMeasures";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_MEASURE";
    public static final String METHOD_OF_INTERPRETATION_RELATIONSHIP_TYPE = "INTERPRETED_AS";

    @Relationship(type = METHOD_OF_INTERPRETATION_RELATIONSHIP_TYPE)
    private final Set<Translation> methodOfInterpretation = new HashSet<>();

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdProperty relatingProperty;

    @Relationship(type = RELATIONSHIP_TYPE)
    private final Set<Measure> relatedMeasures = new HashSet<>();
}
