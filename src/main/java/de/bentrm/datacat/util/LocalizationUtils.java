package de.bentrm.datacat.util;

import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.repository.TextRepository;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.service.MultiLanguageTextRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public final class LocalizationUtils {

    public static final List<Locale.LanguageRange> DEFAULT_LANGUAGE_RANGE = Locale.LanguageRange.parse("de,en-US;q=0.7,en;q=0.3");

    private static MultiLanguageTextRepository multiLanguageTextRepository;
    private static TextRepository textRepository;
    private static ObjectRepository repository;
    private static LanguageRepository languageRepository;

    private LocalizationUtils(MultiLanguageTextRepository multiLanguageTextRepository,
                              TextRepository textRepository,
                              LanguageRepository languageRepository,
                              ObjectRepository repository) {
        this.multiLanguageTextRepository = multiLanguageTextRepository;
        this.textRepository = textRepository;
        this.repository = repository;
        this.languageRepository = languageRepository;
    }

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
    public static XtdText getTranslation(@NotNull String id) {
        return getTranslation(DEFAULT_LANGUAGE_RANGE, id);
    }

    @Nullable
    public static XtdText getTranslation(@NotNull List<Locale.LanguageRange> priorityList, @NotNull String id) {
     
        XtdMultiLanguageText mText = multiLanguageTextRepository.findById(id).orElse(null);

        final Map<Locale, XtdText> translationMap = mText.getTexts().stream()
        .map(t -> textRepository.findById(t.getId()).orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(XtdText::getLocale, Function.identity()));

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
