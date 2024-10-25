package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SetMajorVersionInput {
    @NotNull String catalogEntryId;
    @NotNull int majorVersion;
}
