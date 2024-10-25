package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;

import de.bentrm.datacat.catalog.domain.Enums.XtdRelationshipKindEnum;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdRelationshipType.LABEL)
public class XtdRelationshipType extends XtdConcept {

    public static final String LABEL = "XtdRelationshipType";

    @ToString.Include
    private XtdRelationshipKindEnum kind;
}
