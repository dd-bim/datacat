package de.bentrm.datacat.catalog.service;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdRelationshipType;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.Enums.XtdRelationshipKindEnum;

public interface RelationshipToSubjectRecordService extends SimpleRecordService<XtdRelationshipToSubject> {

        /**
     * Creates a new relationship record.
     * @param id The entity id of the relationship node.
     * @param relatingRecordId The id of the catalog record node that this relationships originates from.
     * @param relatedRecordIds The ids of the catalog records that are members of this relationship.
     * @return The new relationship entity.
     */
    XtdRelationshipToSubject addRecord(@NotBlank String id,
                @NotBlank String relatingRecordId,
                @NotEmpty List<@NotBlank String> relatedRecordIds);

    List<XtdSubject> getScopeSubjects(@NotNull XtdRelationshipToSubject relationshipToSubject);

    List<XtdSubject> getTargetSubjects(@NotNull XtdRelationshipToSubject relationshipToSubject);

    @NotNull XtdSubject getConnectingSubject(@NotNull XtdRelationshipToSubject relationshipToSubject);

    XtdRelationshipType getRelationshipType(@NotNull XtdRelationshipToSubject relationshipToSubject);

    XtdRelationshipToSubject addRelationshipType(@NotNull XtdRelationshipToSubject relationshipToSubject, @NotNull XtdRelationshipKindEnum relationshipKind);

    XtdRelationshipToSubject addRelationshipTypeByName(@NotNull XtdRelationshipToSubject relationshipToSubject, @NotNull String name, @NotNull XtdRelationshipKindEnum relationshipKind);

    XtdRelationshipToSubject setRelatedRecords(@NotNull XtdRelationshipToSubject relationshipToSubject, @NotEmpty List<@NotBlank String> relatedRecordIds);

    Long countRelationshipsUsingRelationshipType(@NotBlank String relationshipTypeId);
}
