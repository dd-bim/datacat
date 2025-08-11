package de.bentrm.datacat.catalog.service;

import jakarta.validation.constraints.NotNull;
import java.util.List;

import de.bentrm.datacat.catalog.domain.XtdSubdivision;

public interface SubdivisionRecordService extends SimpleRecordService<XtdSubdivision> {

    List<XtdSubdivision> getSubdivisions(@NotNull XtdSubdivision subdivision);
}
