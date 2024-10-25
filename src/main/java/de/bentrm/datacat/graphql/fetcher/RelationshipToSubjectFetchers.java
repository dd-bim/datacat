package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.RelationshipToSubjectRecordService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RelationshipToSubjectFetchers extends AbstractFetchers<XtdRelationshipToSubject> {

    private final DataFetcher<XtdSubject> connectingSubject;
    private final DataFetcher<List<XtdRoot>> targetSubjects;
    private final DataFetcher<List<XtdSubject>> scopeSubjects;

    public RelationshipToSubjectFetchers(RelationshipToSubjectRecordService entityService,
                            CatalogService catalogService) {
        super(entityService);

        this.connectingSubject = environment -> {
            final XtdRelationshipToSubject source = environment.getSource();
            final String id = source.getConnectingSubject().getId();
            final CatalogRecord collection = catalogService.getEntryById(id).orElseThrow();
            return (XtdSubject) collection;
        };

        this.targetSubjects = environment -> {
            final XtdRelationshipToSubject source = environment.getSource();
            final List<String> targetSubjects = source.getTargetSubjects().stream()
                    .map(CatalogRecord::getId)
                    .collect(Collectors.toList());
            return catalogService.getAllRootItemsById(targetSubjects);
        };

        this.scopeSubjects = environment -> {
            final XtdRelationshipToSubject source = environment.getSource();
            return entityService.getScopeSubjects(source);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdRelationshipToSubject";
    }

    @Override
    public String getFetcherName() {
        return "getRelationshipToSubject";
    }

    @Override
    public String getListFetcherName() {
        return "findRelationshipToSubjects";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());

        fetchers.put("connectingSubject", connectingSubject);
        fetchers.put("targetSubjects", targetSubjects);
        fetchers.put("scopeSubjects", scopeSubjects);

        return fetchers;
    }
}
