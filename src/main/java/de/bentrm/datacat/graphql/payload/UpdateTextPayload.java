package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.XtdText;
import lombok.Data;

@Data
public class UpdateTextPayload {
    XtdText catalogEntry;
}
