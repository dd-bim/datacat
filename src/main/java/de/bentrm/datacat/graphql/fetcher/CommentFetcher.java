package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.Translation;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Locale;

public class CommentFetcher extends LocalizedAttributeFetcher implements DataFetcher<String> {
    @Override
    public String get(DataFetchingEnvironment environment) {
        final CatalogRecord source = environment.getSource();
        final List<Locale.LanguageRange> priorityList = getPriorityList(environment);
        return source
                .getComment(priorityList)
                .map(Translation::getValue)
                .orElse(null);
    }
}
