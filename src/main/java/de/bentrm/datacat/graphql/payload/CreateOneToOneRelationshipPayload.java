package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.XtdRelSequences;
import lombok.Data;

@Data
public class CreateOneToOneRelationshipPayload {
    XtdRelSequences relationship;
}
