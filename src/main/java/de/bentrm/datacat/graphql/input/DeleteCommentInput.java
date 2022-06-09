package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteCommentInput {
    @NotNull String catalogEntryId;
    @NotNull String commentId;
}
