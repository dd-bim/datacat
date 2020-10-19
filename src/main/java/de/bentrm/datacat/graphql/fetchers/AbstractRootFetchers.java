package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.AssociatesService;
import de.bentrm.datacat.catalog.service.CollectsService;
import de.bentrm.datacat.catalog.service.ComposesService;
import de.bentrm.datacat.catalog.service.QueryService;
import de.bentrm.datacat.graphql.fetcher.AssociatesFetcher;
import de.bentrm.datacat.graphql.fetcher.CollectsFetcher;
import de.bentrm.datacat.graphql.fetcher.ComposesFetcher;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public abstract class AbstractRootFetchers<T extends XtdRoot, S extends QueryService<T>>
        extends AbstractEntityFetchers<T, S> {

    private CollectsFetcher collectsFetcher;
    private AssociatesFetcher associatesFetcher;
    private ComposesFetcher composesFetcher;

    public AbstractRootFetchers(S entityService) {
        super(entityService);
    }

    @Autowired
    public void setCollectsFetcher(CollectsService collectsService) {
        this.collectsFetcher = new CollectsFetcher(collectsService);
    }

    @Autowired
    public void setAssociatesFetcher(AssociatesService service) {
        this.associatesFetcher = new AssociatesFetcher(service);
    }

    @Autowired
    public void setComposesFetcher(ComposesService service) {
        this.composesFetcher = new ComposesFetcher(service);
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("collectedBy", collectsFetcher.collectedBy());
        fetchers.put("associates", associatesFetcher.associates());
        fetchers.put("associatedBy", associatesFetcher.associatedBy());
        fetchers.put("composes", composesFetcher.composes());
        fetchers.put("composedBy", composesFetcher.composedBy());
        return fetchers;
    }
}
