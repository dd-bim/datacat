package de.bentrm.datacat.dto;

import javax.validation.constraints.NotBlank;

public class LanguageInputDto {

    @NotBlank
    private String id;

    @NotBlank
    private String languageNameInEnglish;

    @NotBlank
    private String languageNameInSelf;

    public String getId() {
        return id;
    }

    public String getLanguageNameInEnglish() {
        return languageNameInEnglish;
    }

    public String getLanguageNameInSelf() {
        return languageNameInSelf;
    }
}
