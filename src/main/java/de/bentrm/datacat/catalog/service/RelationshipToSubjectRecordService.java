package de.bentrm.datacat.catalog.service;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdRelationshipType;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.Enums.XtdRelationshipKindEnum;

public interface RelationshipToSubjectRecordService extends RelationshipRecordService<XtdRelationshipToSubject> {

    List<XtdSubject> getScopeSubjects(@NotNull XtdRelationshipToSubject relationshipToSubject);

    List<XtdSubject> getTargetSubjects(@NotNull XtdRelationshipToSubject relationshipToSubject);

    @NotNull XtdSubject getConnectingSubject(@NotNull XtdRelationshipToSubject relationshipToSubject);

    @NotNull XtdRelationshipType getRelationshipType(@NotNull XtdRelationshipToSubject relationshipToSubject);

    XtdRelationshipToSubject addRelationshipType(@NotNull XtdRelationshipToSubject relationshipToSubject, @NotNull XtdRelationshipKindEnum relationshipKind);
}
