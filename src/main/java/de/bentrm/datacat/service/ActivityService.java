package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdActivity;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

public interface ActivityService extends CrudEntityService<XtdActivity, RootInput, RootUpdateInput> {
}
