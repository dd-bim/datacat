package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.domain.XtdValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PayloadMapper {

    PayloadMapper INSTANCE = Mappers.getMapper(PayloadMapper.class);

    @Mapping(source = "item", target = "catalogEntry")
    CreateEntryPayload toCreateEntryPayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteCatalogEntryPayload toDeleteEntryPayload(CatalogRecord item);

    @Mapping(source = "relationship", target = "relationship")
    CreateRelationshipPayload toCreateRelationshipPayload(XtdRelationship relationship);

    @Mapping(source = "relationship", target = "relationship")
    SetRelatedEntriesPayload toSetRelatedEntriesPayload(XtdRelationship relationship);

    @Mapping(source = "relationship", target = "relationship")
    DeleteRelationshipPayload toDeleteRelationshipPayload(XtdRelationship relationship);

    @Mapping(source = "item", target = "catalogEntry")
    SetVersionPayload toSetVersionPayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    AddNamePayload toAddNamePayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateNamePayload toUpdateNamePayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteNamePayload toDeleteNamePayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    AddDescriptionPayload toAddDescriptionPayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateDescriptionPayload toUpdateDescriptionPayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteDescriptionPayload toDeleteDescriptionPayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    AddCommentPayload toAddCommentPayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateCommentPayload toUpdateCommentPayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteCommentPayload toDeleteCommentPayload(CatalogRecord item);

    @Mapping(source = "value", target = "catalogEntry")
    SetTolerancePayload toSetTolerancePayload(XtdValue value);

    @Mapping(source = "value", target = "catalogEntry")
    UnsetTolerancePayload toUnsetTolerancePayload(XtdValue value);

    @Mapping(source = "value", target = "catalogEntry")
    SetNominalValuePayload toSetNominalValuePayload(XtdValue value);

    @Mapping(source = "value", target = "catalogEntry")
    UnsetNominalValuePayload toUnsetNominalValuePayload(XtdValue value);

}
