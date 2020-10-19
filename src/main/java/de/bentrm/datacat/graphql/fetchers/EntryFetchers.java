package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.Translation;
import de.bentrm.datacat.graphql.input.ApiInputMapper;
import de.bentrm.datacat.graphql.input.LocalizationInput;
import de.bentrm.datacat.util.LocalizationUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.*;

public abstract class EntryFetchers implements AttributeFetchers {

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.put("name", name());
        fetchers.put("description", description());
        return fetchers;
    }

    public DataFetcher<String> name() {
        return environment -> {
            final CatalogItem source = environment.getSource();
            final List<Locale.LanguageRange> priorityList = getPriorityList(environment);
            return source
                    .getName(priorityList)
                    .map(Translation::getValue)
                    .orElse(source.getId());
        };
    }

    public DataFetcher<String> description() {
        return environment -> {
            final CatalogItem source = environment.getSource();
            final List<Locale.LanguageRange> priorityList = getPriorityList(environment);
            return source
                    .getDescription(priorityList)
                    .map(Translation::getValue)
                    .orElse(null);
        };
    }

    private List<Locale.LanguageRange> getPriorityList(DataFetchingEnvironment environment) {
        List<Locale.LanguageRange> priorityList = new ArrayList<>();
        if (environment.containsArgument("input")) {
            final Map<String, Object> argument = environment.getArgument("input");
            final LocalizationInput localizationInput = ApiInputMapper.INSTANCE.toLocalizationInput(argument);
            final List<Locale.LanguageRange> requestedPriorityList = localizationInput.getPriorityList();
            priorityList.addAll(0, requestedPriorityList);
        } else {
            priorityList.addAll(LocalizationUtils.DEFAULT_LANGUAGE_RANGE);
        }
        return priorityList;
    }
}
