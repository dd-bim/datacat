package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.auth.domain.Role;
import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.auth.service.AccountStatus;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.graphql.dto.LocalizedTextDto;
import de.bentrm.datacat.service.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ValueMapper {
    ValueMapper INSTANCE = Mappers.getMapper(ValueMapper.class);

    default List<LocalizedTextDto> toLocalizedTextList(Map<String, String> translations) {
        return translations.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    final LocalizedTextDto dto = new LocalizedTextDto();
                    dto.setLanguageTag(entry.getKey());
                    dto.setText(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    default Map<String,String> toTranslationMap(List<LocalizedTextDto> dtos) {
        Map<String, String> translations = new HashMap<>();
        dtos.forEach(dto -> translations.put(dto.getLanguageTag(), dto.getText()));
        return translations;
    }

    Tag toTag(TagDto dto);

    default TagDto toDto(Tag tag) {
        final Locale locale = LocaleContextHolder.getLocale();
        final TagDto dto = new TagDto();

        dto.setId(tag.getId());
        dto.setCreated(tag.getCreated());
        dto.setCreatedBy(tag.getCreatedBy());
        dto.setLastModified(tag.getLastModified());
        dto.setLastModifiedBy(tag.getLastModifiedBy());

        dto.setScope(tag.getScope());

        dto.setLocalizedName(tag.getLocalizedName(locale));
        dto.setNames(toLocalizedTextList(tag.getNames()));

        dto.setLocalizedDescription(tag.getLocalizedDescription(locale));
        dto.setDescriptions(toLocalizedTextList(tag.getDescriptions()));

        return dto;

    }

    void setProperties(TagDto dto, @MappingTarget Tag tag);

    @ValueMappings({
            @ValueMapping(source = "SUPERADMIN", target = "Admin"),
            @ValueMapping(source = "ADMIN", target = "Admin"),
            @ValueMapping(source = "USER", target = "Verified"),
            @ValueMapping(source = "READONLY", target = "Unverified")
    })
    AccountStatus toAccountStatus(Role role);

    default AccountStatus toAccountStatus(Collection<Role> roles) {
        if (roles.contains(Role.SUPERADMIN) || roles.contains(Role.ADMIN)) return AccountStatus.Admin;
        if (roles.contains(Role.USER)) return AccountStatus.Verified;
        return AccountStatus.Unverified;
    }

    @Mapping(target = "username", source = "username")
    @Mapping(target = "profile", source = ".")
    @Mapping(target = "status", source = "roles")
    AccountDto toAccountDto(User user);

    void setProperties(AccountUpdateDto dto, @MappingTarget User user);

    ProfileDto toProfileDto(User user);

    void setProperties(ProfileUpdateDto dto, @MappingTarget User user);
}
