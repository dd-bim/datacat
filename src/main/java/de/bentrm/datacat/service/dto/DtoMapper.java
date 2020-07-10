package de.bentrm.datacat.service.dto;

import de.bentrm.datacat.domain.Role;
import de.bentrm.datacat.domain.User;
import de.bentrm.datacat.service.AccountStatus;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

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
