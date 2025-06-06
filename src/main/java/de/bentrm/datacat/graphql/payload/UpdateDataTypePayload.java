package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import lombok.Data;

@Data
public class UpdateDataTypePayload {
    XtdProperty catalogEntry;
}
