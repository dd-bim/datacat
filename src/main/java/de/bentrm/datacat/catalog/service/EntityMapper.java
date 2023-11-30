package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.service.value.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EntityMapper {

    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    default void setProperties(CatalogRecordProperties dto, @MappingTarget CatalogItem item) {
        setProperties(item, dto.getId(), dto.getVersion(), dto.getNames(), dto.getDescriptions(), dto.getComments());
    }

    default void setProperties(OneToManyRelationshipValue dto, CatalogItem item) {
        setProperties(item, dto.getId(), dto.getVersion(), dto.getNames(), dto.getDescriptions(), dto.getComments());
    }

    default void setProperties(CatalogItem item, String id, VersionValue version, List<TranslationValue> names, List<TranslationValue> descriptions, List<TranslationValue> comments) {
        if (id != null) {
            item.setId(id);
        }
        if (version != null) {
            setVersion(version, item);
        }
        if (names != null) {
            names.forEach(x -> item.addName(x.getId(), x.getLocale(), x.getValue()));
        }
        if (descriptions != null) {
            descriptions.forEach(x -> item.addDescription(x.getId(), x.getLocale(), x.getValue()));
        }
        if (comments != null) {
            comments.forEach(x -> item.addComment(x.getId(), x.getLocale(), x.getValue()));
        }
    }

    void setVersion(VersionValue version, @MappingTarget CatalogItem item);
}
