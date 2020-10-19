package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class HierarchyFilterInput {
    @NotNull @Valid HierarchyRootNodeFilterInput rootNodeFilter;
}
