package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SetMinorVersionInput {
    @NotNull String catalogEntryId;
    @NotNull int minorVersion;
}
