package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateEntryInput {
    @NotNull CatalogRecordType catalogEntryType;
    @NotNull @Valid CatalogEntryPropertiesInput properties;
    List<@NotBlank String> tags;
}
