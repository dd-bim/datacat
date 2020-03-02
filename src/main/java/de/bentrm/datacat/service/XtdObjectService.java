package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.*;
import de.bentrm.datacat.dto.*;
import org.springframework.data.domain.Page;

public interface XtdObjectService extends NamedEntityService<XtdObject> {

    Page<XtdObject> findByRelDocumentsId(String id, int pageNumber, int pageSize);
    Page<XtdObject> findByRelGroupsId(String id, int pageNumber, int pageSize);

    XtdActor createActor(XtdActorInputDto dto);
    XtdActor deleteActor(String id);

    XtdActivity createActivity(XtdActivityInputDto dto);
    XtdActivity deleteActivity(String id);

    XtdSubject createSubject(XtdSubjectInputDto dto);
    XtdSubject deleteSubject(String id);
    Page<XtdSubject> findAllSubjects(int pageNumber, int pageSize);
    Page<XtdSubject> findSubjectsByTerm(String term, int pageNumber, int pageSize);
}
