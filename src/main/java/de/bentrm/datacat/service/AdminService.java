package de.bentrm.datacat.service;

import org.springframework.security.access.prepost.PreAuthorize;

public interface AdminService {

    @PreAuthorize("hasRole('SUPERADMIN')")
    void purgeDatabase();

}
