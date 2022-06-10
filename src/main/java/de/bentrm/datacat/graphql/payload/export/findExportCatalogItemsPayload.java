package de.bentrm.datacat.graphql.payload.export;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import lombok.Data;

import java.util.List;

@Data
public class findExportCatalogItemsPayload {
    List<CatalogItem> nodes;
    List<List<String>> paths;
}
