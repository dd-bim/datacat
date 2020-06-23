package de.bentrm.datacat.domain;

import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;

@Component
public class PreSaveEventListener extends EventListenerAdapter {

	@Autowired
	private AuditorAware<String> auditorAware;

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

		if (obj instanceof XtdRoot entity) {
			if (entity.getVersionId() == null) {
				entity.setVersionId("1");
			}
			if (entity.getVersionDate() == null) {
				entity.setVersionDate(String.valueOf(LocalDate.now().getYear()));
			}
		}
	}

}
