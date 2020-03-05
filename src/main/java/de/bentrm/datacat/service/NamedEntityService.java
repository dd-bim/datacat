package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.NamedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NamedEntityService<T extends NamedEntity> {

    Page<T> findByTerm(String match, Pageable pageable);

}
