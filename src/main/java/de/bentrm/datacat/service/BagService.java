package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

public interface BagService extends CrudEntityService<XtdBag, RootInput, RootUpdateInput> {
}
