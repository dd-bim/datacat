package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateMinorVersionInput {
    @NotNull String catalogEntryId;
    @NotNull int minorVersion;
}
