package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdNest;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.service.SubjectService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SubjectFetchers extends AbstractFetchers<XtdSubject> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<List<XtdNest>> groupOfProperties;
    private final DataFetcher<List<XtdProperty>> properties;

    public SubjectFetchers(SubjectService queryService,
                            RootFetchersDelegate rootFetchersDelegate,
                            ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.groupOfProperties = environment -> {
            final XtdSubject source = environment.getSource();
            return queryService.getGroupOfProperties(source);
        };

        this.properties = environment -> {
            final XtdSubject source = environment.getSource();
            return queryService.getProperties(source);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdSubject";
    }

    @Override
    public String getFetcherName() {
        return "getSubject";
    }

    @Override
    public String getListFetcherName() {
        return "findSubjects";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.putAll(super.getAttributeFetchers());
        fetchers.putAll(rootFetchersDelegate.getFetchers());
        fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("groupOfProperties", groupOfProperties);
        fetchers.put("properties", properties);
        return fetchers;
    }
}
