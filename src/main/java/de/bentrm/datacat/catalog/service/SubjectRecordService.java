package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdNest;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface SubjectRecordService extends SimpleRecordService<XtdSubject> {

    List<XtdNest> getGroupOfProperties(@NotNull XtdSubject subject);

    List<XtdProperty> getProperties(@NotNull XtdSubject subject);

}
