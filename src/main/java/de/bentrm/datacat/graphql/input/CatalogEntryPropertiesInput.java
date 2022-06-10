package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CatalogEntryPropertiesInput {
    String id;
    @Valid VersionInput version;
    @NotEmpty List<@NotNull @Valid TranslationInput> names;
    List<@NotNull @Valid TranslationInput> descriptions;
    List<@NotNull @Valid TranslationInput> comments;
}
