package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdClassification;
import de.bentrm.datacat.catalog.domain.XtdRelClassifies;
import de.bentrm.datacat.catalog.service.ClassificationRecordService;
import de.bentrm.datacat.catalog.service.ClassifiesService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ClassificationFetchers extends AbstractFetchers<XtdClassification> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;

    private final RelationshipFetcher<XtdRelClassifies> classifiesFetcher;



    public ClassificationFetchers(ClassificationRecordService queryService,
                                  RootFetchersDelegate rootFetchersDelegate,
                                  ObjectFetchersDelegate objectFetchersDelegate,
                                  ClassifiesService classifiesService) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.classifiesFetcher = new RelationshipFetcher<>(classifiesService) {
            @Override
            public Connection<XtdRelClassifies> get(DataFetchingEnvironment environment) {
                final XtdClassification source = environment.getSource();
                final Set<XtdRelClassifies> fieldValues = source.getClassifies();
                return get(fieldValues, environment);
            }
        };
    }


    @Override
    public String getTypeName() {
        return "XtdClassification";
    }

    @Override
    public String getFetcherName() {
        return "getClassification";
    }

    @Override
    public String getListFetcherName() {
        return "findClassifications";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.putAll(super.getAttributeFetchers());
        fetchers.putAll(rootFetchersDelegate.getFetchers());
        fetchers.putAll(objectFetchersDelegate.getFetchers());

        fetchers.put("classifies", classifiesFetcher);

        return fetchers;
    }
}
