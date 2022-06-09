package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.auth.service.dto.AccountUpdateDto;
import de.bentrm.datacat.auth.service.dto.ProfileUpdateDto;
import de.bentrm.datacat.auth.specification.UserSpecification;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.input.HierarchyRootNodeFilterInput;
import de.bentrm.datacat.graphql.input.verification.*;
import de.bentrm.datacat.graphql.input.SearchInput;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import javax.validation.constraints.NotNull;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SpecificationMapper {
    SpecificationMapper INSTANCE = Mappers.getMapper(SpecificationMapper.class);

    TagSpecification toTagSpec(@NotNull FilterInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull SearchInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull FilterInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull HierarchyRootNodeFilterInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull findMissingTagsNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull findMissingEnglishNameNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull findMultipleIDsNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull findMissingDescriptionNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull findMissingEnglishDescriptionNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull findMultipleNamesNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogItemSpecification(@NotNull findMultipleNamesAcrossClassesNodeTypeFilterInput input);

    UserSpecification toSpecification(@NotNull AccountFilterInput filter);

    AccountUpdateDto toDto(AccountUpdateInput input);

    ProfileUpdateDto toDto(ProfileUpdateInput input);

}
