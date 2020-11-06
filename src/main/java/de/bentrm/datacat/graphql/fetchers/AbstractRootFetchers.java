package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.*;
import de.bentrm.datacat.graphql.fetcher.AssociatesFetcher;
import de.bentrm.datacat.graphql.fetcher.CollectsFetcher;
import de.bentrm.datacat.graphql.fetcher.ComposesFetcher;
import de.bentrm.datacat.graphql.fetcher.DocumentsFetcher;
import graphql.schema.DataFetcher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Getter
@Slf4j
public abstract class AbstractRootFetchers<T extends XtdRoot, S extends QueryService<T>>
        extends AbstractFetchers<T, S> {

    private AssociatesFetcher associatesFetcher;
    private CollectsFetcher collectsFetcher;
    private ComposesFetcher composesFetcher;
    private DocumentsFetcher documentsFetcher;

    public AbstractRootFetchers(S entityService) {
        super(entityService);
    }

    @Autowired
    public void setAssociatesFetcher(AssociatesService service) {
        this.associatesFetcher = new AssociatesFetcher(service);
    }

    @Autowired
    public void setCollectsFetcher(CollectsService service) {
        this.collectsFetcher = new CollectsFetcher(service);
    }

    @Autowired
    public void setComposesFetcher(ComposesService service) {
        this.composesFetcher = new ComposesFetcher(service);
    }

    @Autowired
    public void setDocumentsFetcher(DocumentsService service) {
        this.documentsFetcher = new DocumentsFetcher(service);
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("associatedBy", associatesFetcher.associatedBy());
        fetchers.put("associates", associatesFetcher.associates());
        fetchers.put("collectedBy", collectsFetcher.collectedBy());
        fetchers.put("composedBy", composesFetcher.composedBy());
        fetchers.put("composes", composesFetcher.composes());
        fetchers.put("documentedBy", documentsFetcher.documentedBy());
        return fetchers;
    }
}
