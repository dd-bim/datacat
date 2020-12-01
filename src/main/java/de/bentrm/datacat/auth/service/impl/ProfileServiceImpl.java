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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

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
        final String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository
                .findByUsername(username)
                .map(user -> valueMapper.toProfileDto(user))
                .orElseThrow();
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
