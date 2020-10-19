package de.bentrm.datacat.util;

import de.bentrm.datacat.catalog.domain.Translation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public final class LocalizationUtils {

    public static final List<Locale.LanguageRange> DEFAULT_LANGUAGE_RANGE = Locale.LanguageRange.parse("de,en-US;q=0.7,en;q=0.3");

    private LocalizationUtils() {}

    public static List<Locale.LanguageRange> getPriorityList(Locale ...locales) {
        List<Locale.LanguageRange> ranges = new ArrayList<>();

        double priority = 1.0;
        for (Locale locale : locales) {
            ranges.add(new Locale.LanguageRange(locale.toLanguageTag(), priority));
            priority = Math.max(0.0, priority - 0.1);
        }
        return ranges;
    }

    @Nullable
    public static Translation getTranslation(@NotNull Collection<Translation> translations) {
        return getTranslation(DEFAULT_LANGUAGE_RANGE, translations);
    }

    @Nullable
    public static Translation getTranslation(@NotNull List<Locale.LanguageRange> priorityList, @NotNull Collection<Translation> translations) {
        final Map<Locale, Translation> translationMap = translations.stream()
                .collect(Collectors.toMap(Translation::getLocale, Function.identity()));
        final Set<Locale> locales = translationMap.keySet();

        if (translationMap.isEmpty()) {
            return null;
        }

        final List<Locale> filteredCandidates = Locale.filter(priorityList, locales);
        if (!filteredCandidates.isEmpty()) {
            return translationMap.get(filteredCandidates.get(0));
        }

        Locale lookupCandidate = Locale.lookup(priorityList, locales);
        if (lookupCandidate != null) {
            return translationMap.get(lookupCandidate);
        }

        return translationMap.getOrDefault(Locale.ENGLISH, null);
    }

    /**
     * Returns the first text in the given locale selecting the most
     * specific language tag including all specifiers and defaulting to
     * the ISO639 language code of the given locale.
     * If no match is acquired the default value is returned.
     *
     * @param texts The map of translations key by language tag.
     * @param defaultValue The default value if no localization is found.
     * @param locales The locales that will be searched for.
     * @return The translated text.
     */
    public static @NotNull String getLocalizedText(Map<String, String> texts, String defaultValue, Locale ...locales) {
        for (Locale locale : locales) {
            final String languageTag = locale.toLanguageTag();
            if (texts.containsKey(languageTag)) {
                return texts.get(languageTag);
            }
        }
        return defaultValue;
    }
}
