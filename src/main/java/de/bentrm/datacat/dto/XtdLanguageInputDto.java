package de.bentrm.datacat.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class XtdLanguageInputDto {

    private String uniqueId;
    private String languageCode;
    private String languageNameInEnglish;
    private String languageNameInSelf;

    public String getUniqueId() {
        return uniqueId;
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
                .append("uniqueId", uniqueId)
                .append("languageCode", languageCode)
                .append("languageNameInEnglish", languageNameInEnglish)
                .append("languageNameInSelf", languageNameInSelf)
                .toString();
    }
}
