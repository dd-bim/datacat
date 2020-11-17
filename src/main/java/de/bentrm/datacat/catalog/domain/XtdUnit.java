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
@NodeEntity(label = XtdUnit.LABEL)
public class XtdUnit extends XtdObject {
    public static final String LABEL = "XtdUnit";

    @Relationship(type = XtdRelAssignsUnits.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelAssignsUnits> assignedTo = new HashSet<>();
}
