package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;

import de.bentrm.datacat.catalog.domain.Enums.XtdRelationshipKindEnum;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdRelationshipType.LABEL)
public class XtdRelationshipType extends XtdConcept {

    public static final String LABEL = "XtdRelationshipType";

    @ToString.Include
    private XtdRelationshipKindEnum kind;
}
