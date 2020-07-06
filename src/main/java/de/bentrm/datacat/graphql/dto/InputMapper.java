package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.domain.Translation;
import de.bentrm.datacat.repository.UserSpecification;
import de.bentrm.datacat.service.Specification;
import de.bentrm.datacat.service.dto.AccountUpdateDto;
import de.bentrm.datacat.service.dto.ProfileUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import javax.validation.constraints.NotNull;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface InputMapper {
    InputMapper INSTANCE = Mappers.getMapper(InputMapper.class);

    Specification toSpecification(@NotNull SearchInput input);

    Specification toSpecification(@NotNull FilterInput input);

    UserSpecification toSpecification(@NotNull AccountFilterInput filter);

    AccountUpdateDto toDto(AccountUpdateInput input);

    ProfileUpdateDto toDto(ProfileUpdateInput input);

    Translation toTranslation(TextInput input);
}
