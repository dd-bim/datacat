package de.bentrm.datacat.domain;

import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListenerAdapter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class PreSaveEventListener extends EventListenerAdapter {

	@Override
	public void onPreSave(Event event) {
		Object obj = event.getObject();

		if (obj instanceof Entity) {
			Entity entity = (Entity) obj;

			LocalDateTime now = LocalDateTime.now();
			if (entity.getCreated() == null) {
				entity.setCreated(now);
			}
			entity.setLastModified(now);
		}

		if (obj instanceof XtdRoot) {
			XtdRoot entity = (XtdRoot) obj;
			if (entity.getVersionId() == null) {
				entity.setVersionId("1");
			}
			if (entity.getVersionDate() == null) {
				entity.setVersionDate(LocalDate.now().toString());
			}
		}
	}

}
