package de.bentrm.datacat.catalog.service.value.verification;

import de.bentrm.datacat.catalog.domain.XtdRoot;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
public class findMultipleNamesValue {
    @NotNull List<XtdRoot> nodes;
    @NotNull List<List<String>> paths;
}
