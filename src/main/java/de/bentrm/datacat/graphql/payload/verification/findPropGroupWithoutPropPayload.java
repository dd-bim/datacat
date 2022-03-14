package de.bentrm.datacat.graphql.payload.verification;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import lombok.Data;

import java.util.List;

@Data
public class findPropGroupWithoutPropPayload {
    List<CatalogItem> nodes;
    List<List<String>> paths;
}
