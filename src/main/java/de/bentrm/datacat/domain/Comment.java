package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NodeEntity(Comment.LABEL)
public class Comment extends Entity implements Comparable<Comment> {

    public static final String LABEL = "Comment";
    public static final String RELATIONSHIP_TYPE = "COMMENTS";

    public enum Status {
        OPEN,
        CLOSED,
        SPAM;
    }

    @NotNull
    private Status status = Status.OPEN;

    @NotBlank
    private String body;

    @NotNull
    public Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull Status status) {
        this.status = status;
    }

    public String getBody() {
        if (status == Status.SPAM) {
            return "";
        }
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int compareTo(@NotNull Comment other) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thisValue = this.created;
        LocalDateTime otherValue = other.created;
        if (thisValue == null) {
            thisValue = now;
        }
        if (otherValue == null) {
            otherValue = now;
        }
        return thisValue.compareTo(otherValue);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("status", status)
                .append("body", body)
                .toString();
    }
}
