package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.graphql.EntryType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateEntryInput {
    @NotNull EntryType entryType;
    @NotNull @Valid EntryPropertiesInput properties;
    List<String> tags;
}
