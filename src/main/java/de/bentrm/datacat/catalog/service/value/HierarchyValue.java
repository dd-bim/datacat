package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
public class HierarchyValue {
    @NotNull List<CatalogItem> nodes;
    @NotNull List<List<String>> paths;
}
