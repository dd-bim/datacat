package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.Translation;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TranslationFetchers implements AttributeFetchers {

    @Override
    public String getTypeName() {
        return "Translation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        return Map.of(
                "language", env -> {
                    Translation value = env.getSource();
                    return value.getLocale();
                }
        );
    }
}
