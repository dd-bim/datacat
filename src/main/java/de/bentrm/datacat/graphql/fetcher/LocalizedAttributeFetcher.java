package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.graphql.input.ApiInputMapper;
import de.bentrm.datacat.graphql.input.LocalizationInput;
import de.bentrm.datacat.util.LocalizationUtils;
import graphql.schema.DataFetchingEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class LocalizedAttributeFetcher {

    public List<Locale.LanguageRange> getPriorityList(DataFetchingEnvironment environment) {
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
