package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdClassification;
import de.bentrm.datacat.catalog.domain.XtdRelClassifies;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ClassificationRecordService;
import de.bentrm.datacat.catalog.service.ClassifiesService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ClassifiesFetchers extends AbstractFetchers<XtdRelClassifies> {

    private final DataFetcher<XtdClassification> relatingClassification;
    private final DataFetcher<List<CatalogRecord>> relatedThings;

    public ClassifiesFetchers(ClassifiesService queryService,
                              ClassificationRecordService classificationService,
                              CatalogService catalogService) {
        super(queryService);

        this.relatingClassification = environment -> {
            final XtdRelClassifies source = environment.getSource();
            final String id = source.getRelatingClassification().getId();
            return classificationService.findById(id).orElseThrow();
        };

        this.relatedThings = environment -> {
            final XtdRelClassifies source = environment.getSource();
            final List<String> relatedValuesId = source.getRelatedThings().stream()
                    .map(CatalogRecord::getId)
                    .collect(Collectors.toList());
            return catalogService.getAllEntriesById(relatedValuesId);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdRelClassifies";
    }

    @Override
    public String getFetcherName() {
        return "getClassifies";
    }

    @Override
    public String getListFetcherName() {
        return "findClassifies";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("relatingClassification", relatingClassification);
        fetchers.put("relatedThings", relatedThings);
        return fetchers;
    }
}
