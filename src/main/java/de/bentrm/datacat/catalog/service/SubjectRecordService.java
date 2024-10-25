package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdSubject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

public interface SubjectRecordService extends SimpleRecordService<XtdSubject> {

    List<XtdProperty> getProperties(@NotNull XtdSubject subject);

    List<XtdRelationshipToSubject> getConnectedSubjects(@NotNull XtdSubject subject);

    List<XtdRelationshipToSubject> getConnectingSubjects(@NotNull XtdSubject subject);

}
