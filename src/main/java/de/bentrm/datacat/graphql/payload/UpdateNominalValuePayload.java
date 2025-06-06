package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.XtdValue;
import lombok.Data;

@Data
public class UpdateNominalValuePayload {
    XtdValue catalogEntry;
}
