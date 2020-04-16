package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdActor;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

public interface ActorService extends CrudEntityService<XtdActor, RootInput, RootUpdateInput> {
}
