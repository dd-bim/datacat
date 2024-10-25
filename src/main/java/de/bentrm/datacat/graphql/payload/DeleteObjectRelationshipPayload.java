package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.AbstractRelationship;
import lombok.Data;

@Data
public class DeleteObjectRelationshipPayload {
    AbstractRelationship relationship;
}
