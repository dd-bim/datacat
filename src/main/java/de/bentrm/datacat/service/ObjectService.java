package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ObjectService {

    Page<XtdObject> findByRelGroupsId(String id, Pageable pageable);

}
