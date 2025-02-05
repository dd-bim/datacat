package de.bentrm.datacat.catalog.service;

import java.util.List;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.Enums.XtdPropertyRelationshipTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public interface RelationshipToPropertyRecordService extends SimpleRecordService<XtdRelationshipToProperty> {

        /**
     * Creates a new relationship record.
     * @param id The entity id of the relationship node.
     * @param relatingRecordId The id of the catalog record node that this relationships originates from.
     * @param relatedRecordIds The ids of the catalog records that are members of this relationship.
     * @return The new relationship entity.
     */
    XtdRelationshipToProperty addRecord(@NotBlank String id,
                @NotBlank String relatingRecordId,
                @NotEmpty List<@NotBlank String> relatedRecordIds);

    XtdProperty getConnectingProperty(@NotNull XtdRelationshipToProperty relationshipToProperty);
    
    List<XtdProperty> getTargetProperties(@NotNull XtdRelationshipToProperty relationshipToProperty);

    XtdRelationshipToProperty addRelationshipType(@NotNull XtdRelationshipToProperty relationshipToProperty, @NotNull XtdPropertyRelationshipTypeEnum relationshipType);

    XtdRelationshipToProperty setRelatedRecords(@NotNull XtdRelationshipToProperty relationshipToProperty, @NotEmpty List<@NotBlank String> relatedRecordIds);
}
