package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.XtdRelAssignsPropertyWithValues;
import lombok.Data;

@Data
public class CreateQualifiedOneToOneRelationshipPayload {
    XtdRelAssignsPropertyWithValues relationship;
}
