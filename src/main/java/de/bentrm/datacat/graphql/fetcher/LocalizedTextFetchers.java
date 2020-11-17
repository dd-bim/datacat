package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.graphql.dto.LocalizedTextDto;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
public class LocalizedTextFetchers implements AttributeFetchers {

    @Override
    public String getTypeName() {
        return "LocalizedText";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        return Map.of(
            "language", environment -> {
                    final LocalizedTextDto localizedTextDto = environment.getSource();
                    final Locale locale = Locale.forLanguageTag(localizedTextDto.getLanguageTag());
                    return locale;
            }
        );

    }
}
