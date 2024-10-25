package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.service.RelationshipToSubjectRecordService;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.RelationshipFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SubjectFetchers extends AbstractFetchers<XtdSubject> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<List<XtdProperty>> properties;
    private final DataFetcher<List<XtdRelationshipToSubject>> connectedSubjects;
    private final DataFetcher<List<XtdRelationshipToSubject>> connectingSubjects;

    public SubjectFetchers(SubjectRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.properties = environment -> {
            final XtdSubject source = environment.getSource();
            return queryService.getProperties(source);
        };

        this.connectedSubjects = environment -> {
            final XtdSubject source = environment.getSource();
            return queryService.getConnectedSubjects(source);
        }; 
    
        this.connectingSubjects = environment -> {
            final XtdSubject source = environment.getSource();
            return queryService.getConnectingSubjects(source);
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
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("properties", properties);
        fetchers.put("connectedSubjects", connectedSubjects);
        fetchers.put("connectingSubjects", connectingSubjects);

        return fetchers;
    }
}
