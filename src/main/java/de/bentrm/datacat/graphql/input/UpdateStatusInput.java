package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.Enums.XtdStatusOfActivationEnum;

@Data
public class UpdateStatusInput {
    @NotNull String catalogEntryId;
    @NotNull XtdStatusOfActivationEnum status;
}
