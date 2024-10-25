package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.XtdConcept;
import lombok.Data;

@Data
public class AddDescriptionPayload {
    XtdConcept catalogEntry;
}
