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
@NodeEntity(label = XtdRelationshipToSubject.LABEL)
public class XtdRelationshipToSubject extends AbstractRelationship {

    public static final String LABEL = "XtdRelationshipToSubject";
    public static final String RELATIONSHIP_TYPE = "CONNECTED_SUBJECTS";
    public static final String RELATIONSHIP_TYPE_OUT = "TARGET_SUBJECTS";

    @ToString.Include
    @Relationship(type = "RELATIONSHIP_TYPE")
    private XtdRelationshipType relationshipType;

    @ToString.Include
    @Relationship(type = "SCOPE_SUBJECTS")
    private final Set<XtdSubject> scopeSubjects = new HashSet<>();

    @ToString.Include
    @Relationship(type = XtdRelationshipToSubject.RELATIONSHIP_TYPE_OUT)
    private final Set<XtdSubject> targetSubjects = new HashSet<>();

    @ToString.Include
    @Relationship(type = XtdRelationshipToSubject.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdSubject connectingSubject;

}
