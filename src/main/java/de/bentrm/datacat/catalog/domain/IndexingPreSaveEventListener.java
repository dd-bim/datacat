package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListenerAdapter;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.bentrm.datacat.catalog.domain.Enums.XtdStatusOfActivationEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * This class supports indexing of catalog entry nodes for multilingual full
 * text search queries.
 */
@Slf4j
@Component
class IndexingPreSaveEventListener extends EventListenerAdapter {

    /**
     * The property {@link XtdObject#getLabels()} represents a map all
     * translations
     * mapped to their language tag. The persisted structure is used to index full
     * text
     * queries honouring the current locale of the user.
     *
     * @param event The persistence event.
     */
    @Override
    public void onPreSave(Event event) {
        if (event.getObject() instanceof XtdObject record) {
            final XtdMultiLanguageText mName = record.getNames().stream().findFirst().orElse(null);
            if (mName == null) {
                return;
            }
            final Map<String, String> labels = mName.getTexts().stream()
                    .collect(Collectors.toMap(text -> text.getLocale().toString(), XtdText::getText));

            if (labels.size() > 0) {
                record.getLabels().clear();
                record.getLabels().putAll(labels);
            }
        }
    }

}
