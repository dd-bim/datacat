package de.bentrm.datacat.graphql.input.verification;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class findMultipleNamesAcrossClassesFilterInput {
    @NotNull @Valid findMultipleNamesAcrossClassesNodeTypeFilterInput nodeTypeFilter;
}
