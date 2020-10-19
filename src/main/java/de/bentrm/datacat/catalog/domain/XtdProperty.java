package de.bentrm.datacat.catalog.domain;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@Getter
@Setter
@NodeEntity(label = XtdProperty.LABEL)
public class XtdProperty extends XtdObject {

    public static final String TITLE = "Property";
    public static final String TITLE_PLURAL = "Properties";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = "RELATED_PROPERTY", direction = Relationship.INCOMING)
    private Set<XtdRelAssignsPropertyWithValues> assignedWithValues;

}
