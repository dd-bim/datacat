package de.bentrm.datacat.graphql.payload;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdText;
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

    @Mapping(source = "item", target = "catalogEntry")
    AddTextPayload toAddTextPayload(XtdObject item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateTextPayload toUpdateTextPayload(XtdText item);

    @Mapping(source = "item", target = "catalogEntry")
    DeleteTextPayload toDeleteTextPayload(XtdText item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateStatusPayload toUpdateStatusPayload(XtdObject item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateMajorVersionPayload toUpdateMajorVersionPayload(XtdObject item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateMinorVersionPayload toUpdateMinorVersionPayload(XtdObject item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateDataTypePayload toUpdateDataTypePayload(XtdProperty item);

    @Mapping(source = "item", target = "catalogEntry")
    UpdateNominalValuePayload toUpdateNominalValuePayload(XtdObject item);
}
