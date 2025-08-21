package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface ValueListRecordService extends SimpleRecordService<XtdValueList> {

    List<XtdOrderedValue> getOrderedValues(@NotNull XtdValueList valueList);

    List<XtdProperty> getProperties(@NotNull XtdValueList valueList);

    Optional<XtdUnit> getUnit(@NotNull XtdValueList valueList);

    Optional<XtdLanguage> getLanguage(@NotNull XtdValueList valueList);

    Optional<XtdValueList> findByIdWithAllRelations(@NotBlank String id);
    
    Optional<XtdValueList> findByIdWithIncomingAndOutgoingRelations(@NotBlank String id);

    @NotNull XtdValueList setOrderedValues(@NotBlank String fromId, @NotEmpty List<String> toIds, @NotNull SimpleRelationType relationType, Integer order);

}
