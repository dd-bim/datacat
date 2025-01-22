package de.bentrm.datacat.auth;

// import org.jetbrains.annotations.NotNull;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.neo4j.core.mapping.callback.BeforeBindCallback;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import de.bentrm.datacat.base.domain.Entity;

@Component
// public class AuditingEventListener extends EventListenerAdapter {
public class AuditingEventListener implements BeforeBindCallback<Entity> {

    private final ApplicationEventPublisher publisher;
    private final AuditorAware<String> principle;

    public AuditingEventListener(ApplicationEventPublisher publisher, AuditorAware<String> principle) {
        this.publisher = publisher;
        this.principle = principle;
    }

    // @Override
    // public void onPreSave(Event event) {
    //     publisher.publishEvent(toAuditEvent(event));
    // }
    @Override
    public @NonNull Entity onBeforeBind(@NonNull Entity entity) {
        publisher.publishEvent(toAuditEvent("BEFORE_BIND", entity));
        return entity;
    }

    // @Override
    // public void onPostSave(Event event) {
    //     publisher.publishEvent(toAuditEvent(event));
    // }

    // @Override
    // public void onPreDelete(Event event) {
    //     publisher.publishEvent(toAuditEvent(event));
    // }

    // @Override
    // public void onPostDelete(Event event) {
    //     publisher.publishEvent(toAuditEvent(event));
    // }

    // @NotNull
    // private AuditApplicationEvent toAuditEvent(Event event) {
    //     final String auditor = principle.getCurrentAuditor().orElse("SYSTEM");
    //     final String lifecycle = event.getLifeCycle().name();
    //     final String serialization = event.getObject().toString();
    //     return new AuditApplicationEvent(auditor, lifecycle, serialization);
    // }
    @NotNull
    private AuditApplicationEvent toAuditEvent(String lifecycle, Entity entity) {
        final String auditor = principle.getCurrentAuditor().orElse("SYSTEM");
        final String serialization = entity.toString();
        return new AuditApplicationEvent(auditor, lifecycle, serialization);
    }
}
