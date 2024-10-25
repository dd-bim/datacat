package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Locale;

public class CommentFetcher extends LocalizedAttributeFetcher implements DataFetcher<String> {

    @Override
    public String get(DataFetchingEnvironment environment) {
        final XtdObject source = environment.getSource();
        final List<Locale.LanguageRange> priorityList = getPriorityList(environment);
        return source
                .getComment(priorityList)
                .map(XtdText::getText)
                .orElse(null);
    }
}
