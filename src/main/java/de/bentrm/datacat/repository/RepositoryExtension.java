package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RepositoryExtension<T extends Entity> {
    Optional<T> findByUID(String uid);
    Page<T> findByUIDs(List<String> uids, Pageable pageable);

    Page<T> findAll();
    Page<T> findAll(Pageable pageable);

    Page<T> findByTerm(String term);
    Page<T> findByTerm(String term, Pageable pageable);
}
