package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SetVersionInput {
    @NotNull String catalogEntryId;
    @NotNull VersionInput version;
}
