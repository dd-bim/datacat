package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PayloadMapper {

    @Mapping(source = "item", target = "entry")
    CreateEntryPayload toCreateEntryPayload(CatalogItem item);

    @Mapping(source = "item", target = "entry")
    DeleteEntryPayload toDeleteEntryPayload(CatalogItem item);

    @Mapping(source = "relationship", target = "relationship")
    CreateOneToOneRelationshipPayload toCreateOneToOneRelationshipPayload(XtdRelSequences relationship);

    @Mapping(source = "relationship", target = "relationship")
    CreateOneToManyRelationshipPayload toCreateOneToManyRelationshipPayload(XtdRelationship relationship);

    @Mapping(source = "relationship", target = "relationship")
    CreateQualifiedOneToOneRelationshipPayload toCreateQualifiedOneToOneRelationshipPayload(XtdRelAssignsPropertyWithValues relationship);

    @Mapping(source = "relationship", target = "relationship")
    DeleteRelationshipPayload toDeleteRelationshipPayload(XtdRelationship relationship);

    @Mapping(source = "item", target = "entry")
    SetVersionPayload toSetVersionPayload(CatalogItem item);

    @Mapping(source = "item", target = "entry")
    AddNamePayload toAddNamePayload(CatalogItem item);

    @Mapping(source = "item", target = "entry")
    UpdateNamePayload toUpdateNamePayload(CatalogItem item);

    @Mapping(source = "item", target = "entry")
    DeleteNamePayload toDeleteNamePayload(CatalogItem item);

    @Mapping(source = "item", target = "entry")
    AddDescriptionPayload toAddDescriptionPayload(CatalogItem item);

    @Mapping(source = "item", target = "entry")
    UpdateDescriptionPayload toUpdateDescriptionPayload(CatalogItem item);

    @Mapping(source = "item", target = "entry")
    DeleteDescriptionPayload toDeleteDescriptionPayload(CatalogItem item);

    @Mapping(source = "value", target = "entry")
    SetTolerancePayload toSetTolerancePayload(XtdValue value);

    @Mapping(source = "value", target = "entry")
    UnsetTolerancePayload toUnsetTolerancePayload(XtdValue value);

    @Mapping(source = "value", target = "entry")
    SetNominalValuePayload toSetNominalValuePayload(XtdValue value);

    @Mapping(source = "value", target = "entry")
    UnsetNominalValuePayload toUnsetNominalValuePayload(XtdValue value);

    HierarchyPayload toHierarchyPayload(HierarchyValue value);

}
