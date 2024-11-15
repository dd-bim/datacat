package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdValue;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface ValueRecordService extends SimpleRecordService<XtdValue> {

    // List<XtdOrderedValue> getOrderedValues(@NotNull XtdValue value);

}
