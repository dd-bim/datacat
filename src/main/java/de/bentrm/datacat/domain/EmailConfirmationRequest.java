package de.bentrm.datacat.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@ToString
@NodeEntity(label = "EmailConfirmation")
public class EmailConfirmationRequest extends Entity {

    public static final Duration VALIDITY_DURATION = Duration.ofHours(1);

    @Relationship(type = "HAS_EMAIL_CONFIRMATION", direction = Relationship.INCOMING)
    private User user;

    private String token;

    private String sentTo;

    @Setter(AccessLevel.NONE)
    private Instant expiredAt;

    @Setter(AccessLevel.NONE)
    private Instant fulfilledAt;

    private EmailConfirmationRequest() {}

    public boolean isExpired() {
        return this.fulfilledAt != null || !Instant.now().isBefore(expiredAt);
    }

    public void fulfill() {
        if (this.isExpired()) {
            throw new IllegalStateException("Email confirmation request is already expired.");
        }
        if (!this.user.getEmail().equals(this.sentTo)) {
            throw new IllegalStateException("The users email address has been changed before this confirmation request has been answered.");
        }

        this.fulfilledAt = Instant.now();
        this.user.setEmailConfirmed(true);
    }

    public static EmailConfirmationRequest of(User user) {
        final EmailConfirmationRequest confirmation = new EmailConfirmationRequest();
        confirmation.user = user;
        confirmation.sentTo = user.getEmail();
        confirmation.token = ConfirmationTokenUtil.generateConfirmationToken();
        confirmation.expiredAt = Instant.now().plus(VALIDITY_DURATION);
        return confirmation;
    }
}
