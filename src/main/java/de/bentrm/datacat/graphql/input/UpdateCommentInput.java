package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class UpdateCommentInput {
    @NotNull String commentId;
    @NotNull String value;
}
