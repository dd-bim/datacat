package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.auth.domain.Role;
import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.auth.service.AccountStatus;
import de.bentrm.datacat.auth.service.dto.AccountDto;
import de.bentrm.datacat.auth.service.dto.AccountUpdateDto;
import de.bentrm.datacat.auth.service.dto.ProfileDto;
import de.bentrm.datacat.auth.service.dto.ProfileUpdateDto;
import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.graphql.input.*;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
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

    void setProperties(PropertyInput properties, @MappingTarget XtdProperty catalogEntry);

    void setProperties(UnitInput properties, @MappingTarget XtdUnit catalogEntry);

    void setProperties(ExternalDocumentInput properties, @MappingTarget XtdExternalDocument catalogEntry);

    void setProperties(CountryInput properties, @MappingTarget XtdCountry catalogEntry);

    void setProperties(CountryInput properties, @MappingTarget XtdSubdivision catalogEntry);

    void setProperties(ValueInput properties, @MappingTarget XtdValue catalogEntry);

    void setProperties(OrderedValueInput properties, @MappingTarget XtdOrderedValue catalogEntry);

    void setProperties(IntervalInput properties, @MappingTarget XtdInterval catalogEntry);

    void setProperties(LanguageInput properties, @MappingTarget XtdLanguage catalogEntry);

    void setProperties(TextInput properties, @MappingTarget XtdText catalogEntry);

    void setProperties(RelationshipTypeInput properties, @MappingTarget XtdRelationshipType catalogEntry);

    void setProperties(RationalInput properties, @MappingTarget XtdRational catalogEntry);
}
