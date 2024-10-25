package de.bentrm.datacat.catalog.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdSubject;

public interface RelationshipToSubjectRecordService extends RelationshipRecordService<XtdRelationshipToSubject> {

    List<XtdSubject> getScopeSubjects(@NotNull XtdRelationshipToSubject relationshipToSubject);
}
