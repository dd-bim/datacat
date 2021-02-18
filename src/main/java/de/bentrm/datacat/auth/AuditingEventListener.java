package de.bentrm.datacat.auth;

import org.jetbrains.annotations.NotNull;
import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListenerAdapter;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class AuditingEventListener extends EventListenerAdapter {

    private final ApplicationEventPublisher publisher;
    private final AuditorAware<String> principle;

    public AuditingEventListener(ApplicationEventPublisher publisher, AuditorAware<String> principle) {
        this.publisher = publisher;
        this.principle = principle;
    }

    @Override
    public void onPreSave(Event event) {
        publisher.publishEvent(toAuditEvent(event));
    }

    @Override
    public void onPostSave(Event event) {
        publisher.publishEvent(toAuditEvent(event));
    }

    @Override
    public void onPreDelete(Event event) {
        publisher.publishEvent(toAuditEvent(event));
    }

    @Override
    public void onPostDelete(Event event) {
        publisher.publishEvent(toAuditEvent(event));
    }

    @NotNull
    private AuditApplicationEvent toAuditEvent(Event event) {
        final String auditor = principle.getCurrentAuditor().orElse("SYSTEM");
        final String lifecycle = event.getLifeCycle().name();
        final String serialization = event.getObject().toString();
        return new AuditApplicationEvent(auditor, lifecycle, serialization);
    }
}
