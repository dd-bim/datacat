package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdLanguage.LABEL)
public class XtdLanguage extends Entity {

    public static final String TITLE = "Language";
    public static final String TITLE_PLURAL = "Languages";
    public static final String LABEL = PREFIX + TITLE;
    public static final String RELATIONSHIP_TYPE = "IN_LANGUAGE";

    private String languageNameInEnglish;

    private String languageNameInSelf;

    public String getLanguageNameInEnglish() {
        return languageNameInEnglish;
    }

    public void setLanguageNameInEnglish(String languageNameInEnglish) {
        this.languageNameInEnglish = languageNameInEnglish;
    }

    public String getLanguageNameInSelf() {
        return languageNameInSelf;
    }

    public void setLanguageNameInSelf(String languageNameInSelf) {
        this.languageNameInSelf = languageNameInSelf;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("languageNameInEnglish", languageNameInEnglish)
                .append("languageNameInSelf", languageNameInSelf)
                .toString();
    }
}
