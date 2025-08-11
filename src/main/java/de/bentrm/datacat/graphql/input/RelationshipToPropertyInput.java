package de.bentrm.datacat.graphql.input;

import lombok.Data;
import de.bentrm.datacat.catalog.domain.Enums.XtdPropertyRelationshipTypeEnum;

@Data
public class RelationshipToPropertyInput {
    XtdPropertyRelationshipTypeEnum relationshipType;
}
