package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdClassification;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.ClassificationService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClassificationDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdClassification, RootInput, RootUpdateInput, ClassificationService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    public ClassificationDataFetcherProvider(ClassificationService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("classifications", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createClassification", add()),
                Map.entry("updateClassification", update()),
                Map.entry("deleteClassification", remove())
        );
    }
}
