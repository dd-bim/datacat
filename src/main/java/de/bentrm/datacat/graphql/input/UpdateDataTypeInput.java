package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import de.bentrm.datacat.catalog.domain.Enums.XtdDataTypeEnum;

@Data
public class UpdateDataTypeInput {
    @NotNull String catalogEntryId;
    @NotNull XtdDataTypeEnum dataType;
}
