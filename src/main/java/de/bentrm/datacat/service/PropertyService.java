package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdProperty;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

public interface PropertyService extends RootService<XtdProperty, RootInput, RootUpdateInput> {
}
