package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class UpdateMajorVersionInput {
    @NotNull String catalogEntryId;
    @NotNull int majorVersion;
}
