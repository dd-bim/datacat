package de.bentrm.datacat.catalog.service.value.export;

import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.domain.ExportItemResult;
import lombok.Value;

import javax.validation.constraints.NotNull;

import java.util.Collection;
import java.util.*;

@Value
public class findExportCatalogItemsValue {
    // @NotNull List<XtdRoot> nodes;
    @NotNull List<ExportItemResult> nodes;
    @NotNull List<List<String>> paths;
}
