package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Locale;

@Data
public class TranslationInput {
    String id;
    @NotNull String languageTag;
    @NotNull String value;

    public String getId() {
        if (id != null && id.isBlank()) return null;
        return id;
    }

    public Locale getLocale() {
        return Locale.forLanguageTag(this.languageTag);
    }
}
