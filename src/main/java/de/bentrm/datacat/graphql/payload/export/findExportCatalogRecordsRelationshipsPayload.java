package de.bentrm.datacat.graphql.payload.export;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import lombok.Data;

import java.util.List;

@Data
public class findExportCatalogRecordsRelationshipsPayload {
    List<CatalogRecord> nodes;
    List<List<String>> paths;
}
