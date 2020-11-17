package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdClassification.LABEL)
public class XtdClassification extends XtdObject {

    public static final String LABEL = "XtdClassification";

    @Relationship(type = XtdRelClassifies.RELATIONSHIP_TYPE)
    private final Set<XtdRelClassifies> classifies = new HashSet<>();

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return Stream
                .of(
                        super.getOwnedRelationships(),
                        classifies
                ).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
