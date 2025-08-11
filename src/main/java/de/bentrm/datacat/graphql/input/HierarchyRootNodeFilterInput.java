package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import lombok.Data;

import java.util.List;

@Data
public class HierarchyRootNodeFilterInput {
    private List<CatalogRecordType> entityTypeIn;
    private List<CatalogRecordType> entityTypeNotIn;
    List<String> idIn;
    List<String> idNotIn;
    List<String> tagged;
}
