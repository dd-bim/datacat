package de.bentrm.datacat.graphql.input;

import lombok.Data;
import de.bentrm.datacat.catalog.domain.Enums.XtdRelationshipKindEnum;

@Data
public class RelationshipToSubjectInput {
    XtdRelationshipKindEnum relationshipType;
    String name;
}
