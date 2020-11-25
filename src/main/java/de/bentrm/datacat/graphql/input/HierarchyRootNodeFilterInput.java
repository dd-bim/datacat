package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.CatalogEntryType;
import lombok.Data;

import java.util.List;

@Data
public class HierarchyRootNodeFilterInput {
    private List<CatalogEntryType> catalogEntryTypeIn;
    private List<CatalogEntryType> catalogEntryTypeNotIn;
    List<String> idIn;
    List<String> idNotIn;
    List<String> tagged;
}
