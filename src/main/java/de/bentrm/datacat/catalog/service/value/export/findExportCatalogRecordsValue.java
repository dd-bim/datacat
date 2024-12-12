package de.bentrm.datacat.catalog.service.value.export;

import de.bentrm.datacat.catalog.domain.ExportItemResult;
import lombok.Value;

import jakarta.validation.constraints.NotNull;

import java.util.*;

@Value
public class findExportCatalogRecordsValue {
    @NotNull List<ExportItemResult> nodes;
    @NotNull List<List<String>> paths;
}
