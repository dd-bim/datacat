package de.bentrm.datacat.graphql.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CommentInput {

    private String body;

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("body", body)
                .toString();
    }
}
