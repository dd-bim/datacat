package de.bentrm.datacat.catalog.service;

import javax.validation.constraints.NotNull;
import java.util.List;

import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.domain.XtdSubdivision;

public interface CountryRecordService extends SimpleRecordService<XtdCountry> {

    List<XtdSubdivision> getSubdivisions(@NotNull XtdCountry country);
}
