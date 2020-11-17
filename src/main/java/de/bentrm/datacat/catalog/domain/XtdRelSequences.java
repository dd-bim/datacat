package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdRelSequences.LABEL)
public class XtdRelSequences extends XtdRelationship {

    public static final String LABEL = "XtdRelSequences";
    public static final String RELATIONSHIP_TYPE = "SEQUENCES";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdActivity relatingActivity;

    @Relationship(type = RELATIONSHIP_TYPE)
    private XtdActivity relatedActivity;
}
