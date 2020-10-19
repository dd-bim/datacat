package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.Translation;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Locale;

public class DescriptionFetcher extends LocalizedAttributeFetcher implements DataFetcher<String> {
    @Override
    public String get(DataFetchingEnvironment environment) throws Exception {
        final CatalogItem source = environment.getSource();
        final List<Locale.LanguageRange> priorityList = getPriorityList(environment);
        return source
                .getDescription(priorityList)
                .map(Translation::getValue)
                .orElse(null);
    }
}
