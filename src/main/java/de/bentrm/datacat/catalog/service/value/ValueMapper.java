package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.auth.domain.Role;
import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.auth.service.AccountStatus;
import de.bentrm.datacat.service.dto.AccountDto;
import de.bentrm.datacat.service.dto.AccountUpdateDto;
import de.bentrm.datacat.service.dto.ProfileDto;
import de.bentrm.datacat.service.dto.ProfileUpdateDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ValueMapper {
    ValueMapper INSTANCE = Mappers.getMapper(ValueMapper.class);

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
