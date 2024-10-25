package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class NameFetcher extends LocalizedAttributeFetcher implements DataFetcher<Optional<String>> {
    @Override
    public Optional<String> get(DataFetchingEnvironment environment) throws Exception {
        final XtdObject source = environment.getSource();
        final List<Locale.LanguageRange> priorityList = getPriorityList(environment);
        return source
                .getName(priorityList)
                .map(XtdText::getText);
    }
}
