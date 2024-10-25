package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdSubdivision;
import de.bentrm.datacat.catalog.service.SubdivisionRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SubdivisionFetchers extends AbstractFetchers<XtdSubdivision> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<List<XtdSubdivision>> subdivisions;

    public SubdivisionFetchers(SubdivisionRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.subdivisions = environment -> {
            XtdSubdivision subdivision = environment.getSource();
            return queryService.getSubdivisions(subdivision);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdSubdivision";
    }

    @Override
    public String getFetcherName() {
        return "getSubdivision";
    }

    @Override
    public String getListFetcherName() {
        return "findSubdivisions";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("subdivisions", subdivisions);
        return fetchers;
    }
}
