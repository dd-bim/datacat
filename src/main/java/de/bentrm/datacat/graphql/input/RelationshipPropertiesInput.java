package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RelationshipPropertiesInput {
    String id;
    @Valid VersionInput version;
    List<@NotNull @Valid TranslationInput> names;
    List<@NotNull @Valid TranslationInput> descriptions;
    List<@NotNull @Valid TranslationInput> comments;
}
