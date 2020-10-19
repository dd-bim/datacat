package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.auth.specification.UserSpecification;
import de.bentrm.datacat.catalog.specification.CatalogItemSpecification;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.input.HierarchyRootNodeFilterInput;
import de.bentrm.datacat.graphql.input.SearchInput;
import de.bentrm.datacat.service.dto.AccountUpdateDto;
import de.bentrm.datacat.service.dto.ProfileUpdateDto;
import de.bentrm.datacat.service.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import javax.validation.constraints.NotNull;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SpecificationMapper {
    SpecificationMapper INSTANCE = Mappers.getMapper(SpecificationMapper.class);

    TagDto toTagDto(String id, TagInput input);

    TagSpecification toTagSpec(@NotNull FilterInput input);

    CatalogItemSpecification toCatalogItemSpecification(@NotNull SearchInput input);

    CatalogItemSpecification toCatalogItemSpecification(@NotNull FilterInput input);

    CatalogItemSpecification toCatalogItemSpecification(@NotNull HierarchyRootNodeFilterInput input);

    UserSpecification toSpecification(@NotNull AccountFilterInput filter);

    AccountUpdateDto toDto(AccountUpdateInput input);

    ProfileUpdateDto toDto(ProfileUpdateInput input);

}
