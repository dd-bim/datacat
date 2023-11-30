package de.bentrm.datacat.graphql.payload.verification;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import lombok.Data;

import java.util.List;

@Data
public class findGroupWithoutSubjectPayload {
    List<CatalogRecord> nodes;
    List<List<String>> paths;
}
