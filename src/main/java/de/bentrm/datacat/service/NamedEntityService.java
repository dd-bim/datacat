package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.NamedEntity;
import de.bentrm.datacat.service.impl.NamedEntityInput;
import org.springframework.data.domain.Page;

public interface NamedEntityService<T extends NamedEntity> {

    T findById(String id);
    Page<T> findAll(String label, int pageNumber, int pageSize);
    Page<T> findByMatch(String label, String match, int pageNumber, int pageSize);

}
