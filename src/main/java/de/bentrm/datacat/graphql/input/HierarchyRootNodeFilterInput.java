package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.graphql.EntryType;
import lombok.Data;

import java.util.List;

@Data
public class HierarchyRootNodeFilterInput {
    private List<EntryType> entryTypeIn;
    private List<EntryType> entryTypeNotIn;
    List<String> idIn;
    List<String> idNotIn;
    List<String> tagged;
}
