package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.catalog.domain.ExportItemResult;
import lombok.Value;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Value
public class ExportCatalogRecordsValue {
    @NotNull List<ExportItemResult> nodes;
}
