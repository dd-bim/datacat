package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.graphql.PageInfo;
import lombok.Value;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Value
public class VerificationConnection {
    @NotNull List<XtdObject> nodes;
    @NotNull List<String> paths;
    @NotNull PageInfo pageInfo;
    @NotNull Long totalElements;
}
