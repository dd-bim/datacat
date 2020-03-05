package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdObject;
import org.springframework.data.domain.Page;

@Deprecated
public interface XtdObjectService extends NamedEntityService<XtdObject> {

    Page<XtdObject> findAll(String label, int pageNumber, int pageSize);

    Page<XtdObject> findByRelDocumentsId(String id, int pageNumber, int pageSize);

    Page<XtdObject> findByRelGroupsId(String id, int pageNumber, int pageSize);
}
