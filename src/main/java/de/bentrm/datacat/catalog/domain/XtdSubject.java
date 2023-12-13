package de.bentrm.datacat.catalog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdSubject.LABEL)
public class XtdSubject extends XtdObject {
    public static final String LABEL = "XtdSubject";

    // List of the properties attached to the subject.
    @ToString.Include
    @Relationship(type = "HAS_PROPERTIES")
    private final Set<XtdProperty> properties = new HashSet<>();

    // List of subjects connected with a qualified relationship.
    @Relationship(type = XtdRelationshipToSubject.RELATIONSHIP_TYPE)
    private final Set<XtdRelationshipToSubject> connectedSubjects = new HashSet<>();

    // Incomming relations
    @Relationship(type = XtdRelationshipToSubject.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelationshipToSubject> connectingSubjects = new HashSet<>();

    // noch nicht implementiert
    // // List of filters used for mapping or automatic classification purpose.
    // @ToString.Include
    // @Relationship(type = "HAS_FILTERS")
    // private final Set<XtdFilter> filters = new HashSet<>();
}
