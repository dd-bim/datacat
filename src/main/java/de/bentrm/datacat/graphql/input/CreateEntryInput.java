package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.CatalogEntryType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateEntryInput {
    @NotNull CatalogEntryType catalogEntryType;
    @NotNull @Valid EntryPropertiesInput properties;
    List<String> tags;
}
