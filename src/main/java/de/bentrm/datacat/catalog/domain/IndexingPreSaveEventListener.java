package de.bentrm.datacat.catalog.domain;

import org.springframework.data.neo4j.core.mapping.callback.BeforeBindCallback;
import org.springframework.lang.NonNull;
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
public class IndexingPreSaveEventListener implements BeforeBindCallback<XtdObject> {


    /**
     * The property {@link XtdObject#getLabels()} represents a map all
     * translations
     * mapped to their language tag. The persisted structure is used to index full
     * text
     * queries honouring the current locale of the user.
     *
     * @param XtdObject The object to persist.
     */
    @Override
    public @NonNull XtdObject onBeforeBind(@NonNull XtdObject record) {
   
            final XtdMultiLanguageText mName = record.getNames().stream().findFirst().orElse(null);
            if (mName == null) {
                return record;
            }
            final Map<String, String> labels = mName.getTexts().stream()
                    .collect(Collectors.toMap(text -> text.getLocale().toString(), XtdText::getText));

            if (!labels.isEmpty()) {
                record.getLabels().clear();
                record.getLabels().putAll(labels);
            }

            return record;
    }

}
