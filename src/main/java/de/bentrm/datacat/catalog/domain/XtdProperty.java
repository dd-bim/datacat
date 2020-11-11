package de.bentrm.datacat.catalog.domain;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NodeEntity(label = XtdProperty.LABEL)
public class XtdProperty extends XtdObject {

    public static final String TITLE = "Property";
    public static final String TITLE_PLURAL = "Properties";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = "RELATED_PROPERTY", direction = Relationship.INCOMING)
    private Set<XtdRelAssignsPropertyWithValues> assignedWithValues;

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return Stream
                .of(super.getOwnedRelationships(), assignedWithValues)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

    }
}
