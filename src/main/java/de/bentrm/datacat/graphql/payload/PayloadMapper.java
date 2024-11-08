package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.AbstractRelationship;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
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

    @Mapping(source = "item", target = "catalogEntry")
    CreateRelationshipPayload toCreateRelationshipPayload(CatalogRecord item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteRelationshipPayload toDeleteRelationshipPayload(CatalogRecord item);

    @Mapping(source = "relationship", target = "relationship")
    DeleteObjectRelationshipPayload toDeleteObjectRelationshipPayload(AbstractRelationship relationship);

    @Mapping(source = "item", target = "catalogEntry")
    AddNamePayload toAddNamePayload(XtdObject item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateNamePayload toUpdateNamePayload(XtdText item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteNamePayload toDeleteNamePayload(XtdText item);

    @Mapping(source = "item", target = "catalogEntry")
    AddDescriptionPayload toAddDescriptionPayload(XtdConcept item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateDescriptionPayload toUpdateDescriptionPayload(XtdText item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteDescriptionPayload toDeleteDescriptionPayload(XtdText item);

    @Mapping(source = "item", target = "catalogEntry")
    AddCommentPayload toAddCommentPayload(XtdObject item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateCommentPayload toUpdateCommentPayload(XtdText item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteCommentPayload toDeleteCommentPayload(XtdText item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateStatusPayload toUpdateStatusPayload(XtdObject item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateMajorVersionPayload toUpdateMajorVersionPayload(XtdObject item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateMinorVersionPayload toUpdateMinorVersionPayload(XtdObject item);
}
