package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import lombok.Data;

@Data
public class UpdateNamePayload {
    CatalogRecord catalogEntry;
}
