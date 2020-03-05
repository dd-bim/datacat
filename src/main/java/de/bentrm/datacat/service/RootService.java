package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.dto.RootInputDto;

import java.util.Optional;

public interface RootService<T extends XtdRoot> extends EntityService<T>, NamedEntityService<T> {

    T create(RootInputDto dto);
    Optional<T> delete(String id);
}
