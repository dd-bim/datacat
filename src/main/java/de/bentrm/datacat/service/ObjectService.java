package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdRoot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ObjectService {

    Page<XtdRoot> findByRelGroupsId(String id, Pageable pageable);

}
