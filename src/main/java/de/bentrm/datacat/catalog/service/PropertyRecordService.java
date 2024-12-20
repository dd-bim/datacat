package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface PropertyRecordService extends SimpleRecordService<XtdProperty> {

    List<XtdSubject> getSubjects(@NotNull XtdProperty property);

    List<XtdValueList> getValueLists(@NotNull XtdProperty property);

    List<XtdUnit> getUnits(@NotNull XtdProperty property);

    List<XtdRelationshipToProperty> getConnectedProperties(@NotNull XtdProperty property);

    List<XtdRelationshipToProperty> getConnectingProperties(@NotNull XtdProperty property);

    Optional<XtdDimension> getDimension(@NotNull XtdProperty property);

    List<XtdSymbol> getSymbols(@NotNull XtdProperty property);

    List<XtdInterval> getIntervals(@NotNull XtdProperty property);

    List<XtdQuantityKind> getQuantityKinds(@NotNull XtdProperty property);
}
