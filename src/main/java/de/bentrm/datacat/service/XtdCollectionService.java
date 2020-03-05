package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.collection.XtdCollection;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Deprecated
public interface XtdCollectionService extends NamedEntityService<XtdCollection> {

    Optional<XtdCollection> findById(String id);

    Page<XtdCollection> findAll(String label, int pageNumber, int pageSize);
}
