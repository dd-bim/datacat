package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdClassification;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

public interface ClassificationService extends CrudEntityService<XtdClassification, RootInput, RootUpdateInput> {

}
