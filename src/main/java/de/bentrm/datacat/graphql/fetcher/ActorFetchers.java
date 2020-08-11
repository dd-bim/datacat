package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdActor;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.ActorService;
import org.springframework.stereotype.Component;

@Component
public class ActorFetchers extends AbstractEntityFetchers<XtdActor, RootInput, RootUpdateInput, ActorService> {

    public ActorFetchers(ActorService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdActor";
    }

    @Override
    public String getQueryName() {
        return "actors";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Actor";
    }
}
