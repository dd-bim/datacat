package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import lombok.Data;

import java.util.List;

@Data
public class HierarchyPayload {
    List<CatalogRecord> nodes;
    List<List<String>> paths;
}
