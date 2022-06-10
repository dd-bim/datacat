package de.bentrm.datacat.graphql.input.verification;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import lombok.Data;

import java.util.List;

@Data
public class findMultipleNamesNodeTypeFilterInput {
    private List<CatalogRecordType> catalogEntryTypeIn;
}

