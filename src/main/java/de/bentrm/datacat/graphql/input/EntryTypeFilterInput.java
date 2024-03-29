package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EntryTypeFilterInput {

    List<@NotNull CatalogRecordType> in;

}
