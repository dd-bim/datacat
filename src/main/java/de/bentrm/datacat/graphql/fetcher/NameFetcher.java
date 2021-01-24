package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.Translation;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class NameFetcher extends LocalizedAttributeFetcher implements DataFetcher<Optional<String>> {
    @Override
    public Optional<String> get(DataFetchingEnvironment environment) throws Exception {
        final CatalogItem source = environment.getSource();
        final List<Locale.LanguageRange> priorityList = getPriorityList(environment);
        return source
                .getName(priorityList)
                .map(Translation::getValue);
    }
}
