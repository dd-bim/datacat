package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

public interface SubjectService extends CrudEntityService<XtdSubject, RootInput, RootUpdateInput> {

}
