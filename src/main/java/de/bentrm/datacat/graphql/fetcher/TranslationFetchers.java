package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.Translation;
import de.bentrm.datacat.service.LanguageService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TranslationFetchers implements AttributeFetchers {

    @Autowired
    private LanguageService languageService;

    @Override
    public String getTypeName() {
        return "Translation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        return Map.of(
                "language", env -> {
                        Translation value = env.getSource();
                        return languageService.findByLanguage(value.getLanguageCode());
                    }
        );
    }
}
