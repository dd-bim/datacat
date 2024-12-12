package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import java.util.Locale;

import jakarta.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdText.LABEL)
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
