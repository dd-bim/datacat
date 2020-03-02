package de.bentrm.datacat.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class XtdLanguageInputDto {

    private String id;
    private String languageCode;
    private String languageNameInEnglish;
    private String languageNameInSelf;

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
