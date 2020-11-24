package de.bentrm.datacat.auth.service;

import de.bentrm.datacat.auth.service.dto.ProfileDto;
import de.bentrm.datacat.auth.service.dto.ProfileUpdateDto;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;

public interface ProfileService {

    @PreAuthorize("isAuthenticated()")
    ProfileDto getProfile();

    @PreAuthorize("#dto.username.equals(principal) or hasRole('ADMIN')")
    ProfileDto updateAccount(@Valid ProfileUpdateDto dto);

}
