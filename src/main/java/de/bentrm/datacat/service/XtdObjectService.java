package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.*;
import de.bentrm.datacat.dto.*;
import org.springframework.data.domain.Page;

public interface XtdObjectService extends NamedEntityService<XtdObject> {

    XtdActor createActor(XtdActorInputDto dto);
    XtdActor deleteActor(String uniqueId);

    XtdActivity createActivity(XtdActivityInputDto dto);
    XtdActivity deleteActivity(String uniqueId);

    XtdSubject createSubject(XtdSubjectInputDto dto);
    XtdSubject deleteSubject(String uniqueId);
    Page<XtdSubject> findAllSubjects(int pageNumber, int pageSize);
    Page<XtdSubject> findByTerm(String term, int pageNumber, int pageSize);
}
