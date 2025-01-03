package de.bentrm.datacat.graphql.payload;

import java.util.List;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import lombok.Data;

@Data
public class VerificationPayload {
    List<CatalogRecord> nodes;
    List<List<String>> paths;
}
