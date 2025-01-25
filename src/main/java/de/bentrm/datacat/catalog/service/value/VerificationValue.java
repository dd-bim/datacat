package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.catalog.domain.XtdObject;
import lombok.Value;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Value
public class VerificationValue {
    @NotNull List<XtdObject> nodes;
    @NotNull List<String> paths;
}
