package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.collection.XtdNest;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

public interface NestService extends CrudEntityService<XtdNest, RootInput, RootUpdateInput> {
}
