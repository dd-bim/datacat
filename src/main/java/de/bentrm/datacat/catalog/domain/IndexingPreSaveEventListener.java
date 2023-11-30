package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListenerAdapter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class supports indexing of catalog entry nodes for multilingual full
 * text search queries.
 */
@Slf4j
@Component
class IndexingPreSaveEventListener extends EventListenerAdapter {

    /**
     * The property {@link CatalogRecord#getLabels()} represents a map all
     * translations
     * mapped to their language tag. The persisted structure is used to index full
     * text
     * queries honouring the current locale of the user.
     *
     * @param event The persistence event.
     */
    @Override
    public void onPreSave(Event event) {
        if (event.getObject() instanceof CatalogRecord catalogRecord) {
            final Map<String, String> labels = catalogRecord.getNames().stream()
                    .collect(Collectors.toMap(Translation::getLanguageTag, Translation::getValue));

            if (labels.size() > 0) {
                catalogRecord.getLabels().clear();
                catalogRecord.getLabels().putAll(labels);
            }
        }
    }
}
