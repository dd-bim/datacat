package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.graphql.EntryType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EntryTypeFilterInput {

    List<@NotNull EntryType> in;

}
