package de.bentrm.datacat.catalog.service.value.export;

import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.domain.ExportRelationshipResult;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
public class findExportCatalogItemsRelationshipsValue {
    @NotNull List<ExportRelationshipResult> nodes;
    @NotNull List<List<String>> paths;
}
