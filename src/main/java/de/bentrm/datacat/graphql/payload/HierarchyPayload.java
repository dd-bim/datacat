package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import lombok.Data;

import java.util.List;

@Data
public class HierarchyPayload {
    List<CatalogItem> nodes;
    List<List<String>> paths;
}
