package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdActor;
import de.bentrm.datacat.catalog.service.NewActorService;
import org.springframework.stereotype.Component;

@Component
public class ActorFetchers extends ObjectFetchers<XtdActor, NewActorService> {

    public ActorFetchers(NewActorService service) {
        super(service);
    }

    @Override
    public String getTypeName() {
        return "XtdActor";
    }

    @Override
    public String getFetcherName() {
        return "actor";
    }

    @Override
    public String getListFetcherName() {
        return "actors";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Actor";
    }
}
