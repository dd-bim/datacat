package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.XtdRelationship;
import lombok.Data;

@Data
public class DeleteRelationshipPayload {
    XtdRelationship relationship;
}
