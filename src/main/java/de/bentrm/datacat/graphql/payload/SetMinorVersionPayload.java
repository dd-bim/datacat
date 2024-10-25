package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.XtdObject;
import lombok.Data;

@Data
public class SetMinorVersionPayload {
    XtdObject catalogEntry;
}
