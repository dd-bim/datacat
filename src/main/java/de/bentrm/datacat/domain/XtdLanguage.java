package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Locale;

public class XtdLanguage {

    private String id;
    private String languageCode;
    private String languageNameInEnglish;
    private String languageNameInSelf;

    public XtdLanguage(Locale locale) {
        this.id = locale.getLanguage();
        this.languageCode = locale.getLanguage();
        this.languageNameInEnglish = locale.getDisplayLanguage(Locale.ENGLISH);
        this.languageNameInSelf = locale.getDisplayLanguage(locale);
    }

    public String getId() {
        return id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getLanguageNameInEnglish() {
        return languageNameInEnglish;
    }

    public String getLanguageNameInSelf() {
        return languageNameInSelf;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("languageCode", languageCode)
                .append("languageNameInEnglish", languageNameInEnglish)
                .append("languageNameInSelf", languageNameInSelf)
                .toString();
    }
}
