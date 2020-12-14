package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.ToleranceType;
import de.bentrm.datacat.catalog.domain.ValueRole;
import de.bentrm.datacat.catalog.domain.ValueType;
import de.bentrm.datacat.catalog.domain.XtdValue;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface ValueRecordService extends SimpleRecordService<XtdValue> {

    @PreAuthorize("hasRole('USER')")
    XtdValue setTolerance(@NotBlank String id, @NotNull ToleranceType toleranceType,
                          String lowerTolerance, String upperTolerance);

    @PreAuthorize("hasRole('USER')")
    XtdValue unsetTolerance(@NotBlank String id);

    @PreAuthorize("hasRole('USER')")
    XtdValue setNominalValue(@NotBlank String id, @NotNull ValueRole valueRole,
                             @NotNull ValueType valueType, String nominalValue);

    @PreAuthorize("hasRole('USER')")
    XtdValue unsetNominalValue(@NotBlank String id);

}
