package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.domain.XtdUnit;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
public interface UnitRecordService extends SimpleRecordService<XtdUnit> {

    List<XtdProperty> getProperties(@NotNull XtdUnit unit);

    Optional<XtdDimension> getDimension(@NotNull XtdUnit unit);

    Optional<XtdMultiLanguageText> getSymbol(@NotNull XtdUnit unit);

    Optional<XtdRational> getCoefficient(@NotNull XtdUnit unit);

    Optional<XtdRational> getOffset(@NotNull XtdUnit unit);
}
