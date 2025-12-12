package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdRelationshipToSubject.LABEL)
public class XtdRelationshipToSubject extends XtdObject {

    public static final String LABEL = "XtdRelationshipToSubject";
    public static final String RELATIONSHIP_TYPE = "CONNECTED_SUBJECTS";
    public static final String RELATIONSHIP_TYPE_OUT = "TARGET_SUBJECTS";

    // @ToString.Include
    @Relationship(type = "RELATIONSHIP_TYPE")
    private XtdRelationshipType relationshipType;

    // @ToString.Include
    @Lazy
    @Relationship(type = "SCOPE_SUBJECTS")
    private Set<XtdSubject> scopeSubjects = new HashSet<>();

    // @ToString.Include
    @Lazy
    @Relationship(type = XtdRelationshipToSubject.RELATIONSHIP_TYPE_OUT)
    private Set<XtdSubject> targetSubjects = new HashSet<>();

    // @ToString.Include
    @Lazy
    @Relationship(type = XtdRelationshipToSubject.RELATIONSHIP_TYPE, direction = Relationship.Direction.INCOMING)
    private XtdSubject connectingSubject;

}
