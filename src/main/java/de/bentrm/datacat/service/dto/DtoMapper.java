package de.bentrm.datacat.service.dto;

import de.bentrm.datacat.domain.Roles;
import de.bentrm.datacat.domain.User;
import de.bentrm.datacat.service.AccountStatus;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    @ValueMappings({
            @ValueMapping(source = "ROLE_SUPERADMIN", target = "Admin"),
            @ValueMapping(source = "ROLE_ADMIN", target = "Admin"),
            @ValueMapping(source = "ROLE_USER", target = "Verified"),
            @ValueMapping(source = "ROLE_READONLY", target = "Unverified")
    })
    AccountStatus toAccountStatus(Roles roles);

    default AccountStatus toAccountStatus(Collection<Roles> roles) {
        if (roles.contains(Roles.ROLE_SUPERADMIN) || roles.contains(Roles.ROLE_ADMIN)) return AccountStatus.Admin;
        if (roles.contains(Roles.ROLE_USER)) return AccountStatus.Verified;
        return AccountStatus.Unverified;
    }

    @Mapping(target = "status", source = "roles")
    AccountDto toDto(User user);

    void setProperties(AccountUpdateDto dto, @MappingTarget User user);
}
