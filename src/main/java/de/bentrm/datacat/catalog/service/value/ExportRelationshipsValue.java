package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.catalog.domain.ExportRelationshipResult;
import lombok.Value;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Value
public class ExportRelationshipsValue {
    @NotNull List<ExportRelationshipResult> nodes;
    @NotNull List<List<String>> paths;
}
