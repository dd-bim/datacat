package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.auth.service.dto.AccountUpdateDto;
import de.bentrm.datacat.auth.service.dto.ProfileUpdateDto;
import de.bentrm.datacat.auth.specification.UserSpecification;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.input.HierarchyRootNodeFilterInput;
import de.bentrm.datacat.graphql.input.verification.*;
import de.bentrm.datacat.graphql.input.SearchInput;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import jakarta.validation.constraints.NotNull;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SpecificationMapper {
    SpecificationMapper INSTANCE = Mappers.getMapper(SpecificationMapper.class);

    TagSpecification toTagSpec(@NotNull FilterInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull SearchInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull FilterInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull HierarchyRootNodeFilterInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull findMissingTagsNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull findMissingEnglishNameNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull findMultipleIDsNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull findMissingDescriptionNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull findMissingEnglishDescriptionNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull findMultipleNamesNodeTypeFilterInput input);

    CatalogRecordSpecification toCatalogRecordSpecification(@NotNull findMultipleNamesAcrossClassesNodeTypeFilterInput input);

    UserSpecification toSpecification(@NotNull AccountFilterInput filter);

    AccountUpdateDto toDto(AccountUpdateInput input);

    ProfileUpdateDto toDto(ProfileUpdateInput input);

}
