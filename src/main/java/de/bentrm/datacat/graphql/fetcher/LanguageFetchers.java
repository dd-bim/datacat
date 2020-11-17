package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.input.ApiInputMapper;
import de.bentrm.datacat.graphql.input.LanguageFilterInput;
import de.bentrm.datacat.graphql.input.LocaleInput;
import graphql.schema.DataFetcher;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LanguageFetchers implements QueryFetchers, AttributeFetchers {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public final static List<Locale> locales = new ArrayList<>();

    static {
        locales.addAll(LocaleUtils.availableLocaleList());
        locales.sort(Comparator.comparing(Locale::toLanguageTag));
    }

    @Autowired
    private ApiInputMapper apiInputMapper;

    @Override
    public String getTypeName() {
        return "Language";
    }

    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.of("languages", environment -> {
            if (environment.containsArgument("input")) {
                final Object argument = environment.getArgument("input");
                final LanguageFilterInput languageFilterInput = objectMapper.convertValue(argument, LanguageFilterInput.class);
                final String query = languageFilterInput.getQuery();
                final List<String> exclude = languageFilterInput.getExcludeLanguageTags();
                final List<Locale> filtered = locales.stream().filter(locale -> {
                    if (query != null && !query.isBlank()) {
                        String sanitizedQuery = query.toLowerCase().trim();
                        if (!locale.toLanguageTag().toLowerCase().contains(sanitizedQuery)
                                && !locale.getDisplayCountry().toLowerCase().contains(sanitizedQuery)
                                && !locale.getDisplayLanguage().toLowerCase().contains(sanitizedQuery)) {
                            return false;
                        }
                    }
                    return exclude == null || !exclude.contains(locale.toLanguageTag());
                }).collect(Collectors.toList());
                return Connection.of(filtered);
            }
            return Connection.of(locales);
        });
    }

    // TODO: Add support and fallback for user defined locales
    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        return Map.of(
                "id", environment -> {
                    final Locale locale = environment.getSource();
                    return locale.toLanguageTag();
                },
                "languageTag", environment -> {
                    final Locale locale = environment.getSource();
                    return locale.toLanguageTag();
                },
                "displayLanguage", environment -> {
                    final Locale locale = environment.getSource();
                    if (environment.containsArgument("input")) {
                        final Map<String, Object> argument = environment.getArgument("input");
                        final LocaleInput input = apiInputMapper.toLocaleInput(argument);
                        final Locale inLocale = input.getLocale();
                        if (inLocale != null) {
                            return locale.getDisplayLanguage(inLocale);
                        }
                    }
                    return locale.getDisplayLanguage(locale);
                },
                "displayCountry", environment -> {
                    final Locale locale = environment.getSource();
                    if (environment.containsArgument("input")) {
                        final Map<String, Object> argument = environment.getArgument("input");
                        final LocaleInput input = apiInputMapper.toLocaleInput(argument);
                        final Locale inLocale = input.getLocale();
                        if (inLocale != null) {
                            return locale.getDisplayCountry(inLocale);
                        }
                    }
                    return locale.getDisplayCountry(locale);
                }
        );
    }
}
