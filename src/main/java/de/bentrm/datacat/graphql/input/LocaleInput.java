package de.bentrm.datacat.graphql.input;

import lombok.Data;
import org.apache.commons.lang3.LocaleUtils;

import javax.validation.constraints.NotBlank;
import java.util.Locale;

@Data
public class LocaleInput {
    @NotBlank String languageTag;

    public Locale getLocale() {
        final Locale locale = Locale.forLanguageTag(languageTag);
        return LocaleUtils.isAvailableLocale(locale) ? locale : null;
    }
}
