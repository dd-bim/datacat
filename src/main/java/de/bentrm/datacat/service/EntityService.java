package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EntityService<T extends Entity> {

    Optional<T> findById(String id);
    Page<T> findAll(Pageable pageable);
    Page<T> findByTerm(String match, Pageable pageable);

}
