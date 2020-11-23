package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.Tag;
import lombok.Value;

@Value
public class DeleteTagPayload {
    Tag tag;
}
