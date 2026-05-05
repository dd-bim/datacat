package de.bentrm.datacat.auth.service.impl;

import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.auth.service.ProfileService;
import de.bentrm.datacat.auth.service.dto.ProfileDto;
import de.bentrm.datacat.auth.service.dto.ProfileUpdateDto;
import de.bentrm.datacat.base.repository.UserRepository;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValueMapper valueMapper;

    @Override
    public ProfileDto getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof Jwt jwt) {
            // Keycloak JWT Token - Virtuelles Profil direkt aus Token-Claims erstellen
            String username = jwt.getClaimAsString("preferred_username");
            if (username == null) {
                username = jwt.getClaimAsString("sub");
            }
            
            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");
            String email = jwt.getClaimAsString("email");
            String organization = jwt.getClaimAsString("organization");
            
            return ProfileDto.builder()
                    .username(username)
                    .firstName(firstName != null ? firstName : "")
                    .lastName(lastName != null ? lastName : "")
                    .email(email != null ? email : "")
                    .organization(organization != null ? organization : "")
                    .build();
        } else {
            // Legacy JWT Token - Profil aus DB laden
            String username = (String) principal;
            return userRepository
                    .findByUsername(username)
                    .map(user -> valueMapper.toProfileDto(user))
                    .orElseThrow(() -> new IllegalArgumentException("No account with username " + username + " found."));
        }
    }

    @Transactional
    @Override
    public ProfileDto updateAccount(@Valid ProfileUpdateDto dto) {
        User user = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("No account found."));
        valueMapper.setProperties(dto, user);
        user = userRepository.save(user);
        return valueMapper.toProfileDto(user);
    }
}
