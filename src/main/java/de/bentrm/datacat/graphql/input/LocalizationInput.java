package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.util.LocalizationUtils;
import lombok.Data;
import org.apache.commons.lang3.LocaleUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Locale;

@Data
public class LocalizationInput {
    @NotEmpty List<@NotBlank String> languageTags;

    public List<Locale.LanguageRange> getPriorityList() {
        final Locale[] locales = getLocales();
        return LocalizationUtils.getPriorityList(locales);
    }

    public Locale[] getLocales() {
        return languageTags.stream()
                .map(Locale::forLanguageTag)
                .filter(LocaleUtils::isAvailableLocale)
                .toArray(Locale[]::new);
    }
}
