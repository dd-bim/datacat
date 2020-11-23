package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.Tag;
import lombok.Value;

@Value
public class RemoveTagPayload {
    CatalogItem entry;
    Tag tag;
}
