package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.service.dto.AccountUpdateDto;
import de.bentrm.datacat.service.dto.ProfileUpdateDto;
import de.bentrm.datacat.specification.CatalogItemSpecification;
import de.bentrm.datacat.specification.UserSpecification;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import javax.validation.constraints.NotNull;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface InputMapper {
    InputMapper INSTANCE = Mappers.getMapper(InputMapper.class);

    CatalogItemSpecification toCatalogItemSpecification(@NotNull SearchInput input);

    CatalogItemSpecification toCatalogItemSpecification(@NotNull FilterInput input);

    UserSpecification toSpecification(@NotNull AccountFilterInput filter);

    AccountUpdateDto toDto(AccountUpdateInput input);

    ProfileUpdateDto toDto(ProfileUpdateInput input);
}
