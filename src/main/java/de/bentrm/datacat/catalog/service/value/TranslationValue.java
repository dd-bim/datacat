package de.bentrm.datacat.catalog.service.value;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Locale;

@Value
public class TranslationValue {
    String id;
    @NotNull Locale locale;
    @NotBlank String value;

    public String getId() {
        return StringUtils.isNoneBlank(id) ? id : null;
    }
}
