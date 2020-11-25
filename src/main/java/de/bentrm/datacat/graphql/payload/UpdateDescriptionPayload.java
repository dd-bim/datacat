package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import lombok.Data;

@Data
public class UpdateDescriptionPayload {
    CatalogItem catalogEntry;
}
