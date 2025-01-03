package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import lombok.Data;

import java.util.List;

@Data
public class VerificationNodeTypeFilterInput {
    private List<CatalogRecordType> catalogEntryTypeIn;
}

