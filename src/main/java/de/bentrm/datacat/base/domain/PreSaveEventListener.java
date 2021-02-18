package de.bentrm.datacat.base.domain;

import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Event listener bound to the data store.
 */
@Component
public class PreSaveEventListener extends EventListenerAdapter {

    @Autowired
    private AuditorAware<String> auditorAware;

    /**
     * Called before save of any database entity.
     * @param event The current persistence event.
     */
    @Override
    public void onPreSave(Event event) {
        final String currentAuditor = auditorAware.getCurrentAuditor().orElse("SYSTEM");
        Instant now = Instant.now();

        Object obj = event.getObject();

        if (obj instanceof Entity entity) {

            if (entity.getCreated() == null) {
                entity.setCreated(now);
            }
            if (entity.getCreatedBy() == null) {
                entity.setCreatedBy(currentAuditor);
            }
            entity.setLastModified(now);
            entity.setLastModifiedBy(currentAuditor);
        }
    }

}
