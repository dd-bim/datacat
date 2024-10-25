package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import java.util.Locale;

import javax.validation.constraints.NotBlank;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdText.LABEL)
public class XtdText extends XtdRoot {

    public static final String LABEL = "XtdText";

    @EqualsAndHashCode.Include
    @ToString.Include
    @NotBlank
    @Property("text")
    private String text;

    @Relationship(type = "LANGUAGE")
    private XtdLanguage language;


    public Locale getLocale() {
        String languageTag = this.language.getCode();
        return Locale.forLanguageTag(languageTag);
    }

}
