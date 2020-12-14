package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PayloadMapper {

    PayloadMapper INSTANCE = Mappers.getMapper(PayloadMapper.class);

    @Mapping(source = "item", target = "catalogEntry")
    CreateEntryPayload toCreateEntryPayload(CatalogItem item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteCatalogEntryPayload toDeleteEntryPayload(CatalogItem item);

    @Mapping(source = "relationship", target = "relationship")
    CreateRelationshipPayload toCreateRelationshipPayload(XtdRelationship relationship);

    @Mapping(source = "relationship", target = "relationship")
    SetRelatedEntriesPayload toSetRelatedEntriesPayload(XtdRelationship relationship);

    @Mapping(source = "relationship", target = "relationship")
    CreateOneToOneRelationshipPayload toCreateOneToOneRelationshipPayload(XtdRelSequences relationship);

    @Mapping(source = "relationship", target = "relationship")
    CreateOneToManyRelationshipPayload toCreateOneToManyRelationshipPayload(XtdRelationship relationship);

    @Mapping(source = "relationship", target = "relationship")
    CreateQualifiedOneToOneRelationshipPayload toCreateQualifiedOneToOneRelationshipPayload(XtdRelAssignsPropertyWithValues relationship);

    @Mapping(source = "relationship", target = "relationship")
    DeleteRelationshipPayload toDeleteRelationshipPayload(XtdRelationship relationship);

    @Mapping(source = "item", target = "catalogEntry")
    SetVersionPayload toSetVersionPayload(CatalogItem item);

    @Mapping(source = "item", target = "catalogEntry")
    AddNamePayload toAddNamePayload(CatalogItem item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateNamePayload toUpdateNamePayload(CatalogItem item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteNamePayload toDeleteNamePayload(CatalogItem item);

    @Mapping(source = "item", target = "catalogEntry")
    AddDescriptionPayload toAddDescriptionPayload(CatalogItem item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateDescriptionPayload toUpdateDescriptionPayload(CatalogItem item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteDescriptionPayload toDeleteDescriptionPayload(CatalogItem item);

    @Mapping(source = "value", target = "catalogEntry")
    SetTolerancePayload toSetTolerancePayload(XtdValue value);

    @Mapping(source = "value", target = "catalogEntry")
    UnsetTolerancePayload toUnsetTolerancePayload(XtdValue value);

    @Mapping(source = "value", target = "catalogEntry")
    SetNominalValuePayload toSetNominalValuePayload(XtdValue value);

    @Mapping(source = "value", target = "catalogEntry")
    UnsetNominalValuePayload toUnsetNominalValuePayload(XtdValue value);

}
