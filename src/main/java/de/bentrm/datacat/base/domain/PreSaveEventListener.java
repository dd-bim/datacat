package de.bentrm.datacat.base.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.neo4j.core.mapping.callback.BeforeBindCallback;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/**
 * Event listener bound to the data store.
 */
@Slf4j
@Component
// public class PreSaveEventListener extends EventListenerAdapter {
public class PreSaveEventListener implements BeforeBindCallback<Entity> {

    @Autowired
    private AuditorAware<String> auditorAware;

    /**
     * Called before save of any database entity.
     * @param event The current persistence event.
     */
    @Override
    // public void onPreSave(Event event) {
    public Entity onBeforeBind(@NonNull Entity entity) {
        final String currentAuditor = auditorAware.getCurrentAuditor().orElse("SYSTEM");
        Instant now = Instant.now();

        // log.info("PreSaveEventListener is called: {}", entity);
        // Object obj = event.getObject();

        // if (obj instanceof Entity entity) {

            if (entity.getCreated() == null) {
                entity.setCreated(now);
            }
            if (entity.getCreatedBy() == null) {
                entity.setCreatedBy(currentAuditor);
            }
            entity.setLastModified(now);
            entity.setLastModifiedBy(currentAuditor);
        // }
        return entity;
    }

}
