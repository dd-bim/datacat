package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.Enums.XtdRelationshipKindEnum;
import lombok.Data;

@Data
public class RelationshipTypeInput {
    XtdRelationshipKindEnum kind;;
}
