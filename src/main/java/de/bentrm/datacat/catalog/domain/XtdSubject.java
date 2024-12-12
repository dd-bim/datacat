package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

// @Getter
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdSubject.LABEL)
public class XtdSubject extends XtdConcept {
    public static final String LABEL = "XtdSubject";

    // List of the properties attached to the subject.
    // @ToString.Include
    @Relationship(type = "PROPERTIES")
    private final Set<XtdProperty> properties = new HashSet<>();

    // List of subjects connected with a qualified relationship.
    // @ToString.Include
    @Relationship(type = XtdRelationshipToSubject.RELATIONSHIP_TYPE)
    private final Set<XtdRelationshipToSubject> connectedSubjects = new HashSet<>();

    // Incomming relations
    // @ToString.Include
    @Relationship(type = XtdRelationshipToSubject.RELATIONSHIP_TYPE_OUT, direction = Relationship.Direction.INCOMING)
    private final Set<XtdRelationshipToSubject> connectingSubjects = new HashSet<>();

    // noch nicht implementiert
    // // List of filters used for mapping or automatic classification purpose.
    // @ToString.Include
    // @Relationship(type = "HAS_FILTERS")
    // private final Set<XtdFilter> filters = new HashSet<>();

}
