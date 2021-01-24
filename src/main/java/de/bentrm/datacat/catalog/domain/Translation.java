package de.bentrm.datacat.catalog.domain;

import de.bentrm.datacat.base.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.LocaleUtils;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = "Translation")
public class Translation extends Entity {

    @EqualsAndHashCode.Include
    @ToString.Include
    @NotBlank
    @Property("languageCode")
    private final String languageTag;

    @EqualsAndHashCode.Include
    @ToString.Include
    @NotBlank
    @Property("label")
    private String value;

    @PersistenceConstructor
    protected Translation(String id, String languageTag, String value) {
        this.setId(id);
        this.languageTag = languageTag;
        this.value = value;
    }

    public Translation(@Nullable String id, @NotNull Locale locale, @NotBlank String value) {
        Assert.isTrue(id == null || !id.isBlank(), "An id may not be blank.");
        Assert.notNull(locale, "A locale must be provided at creation time.");
        Assert.isTrue(LocaleUtils.isAvailableLocale(locale), "Locale must be available at creation time.");
        Assert.hasText(value, "The translation value may not be blank.");

        if (id != null) {
            this.setId(id.trim());
        }
        this.languageTag = locale.toLanguageTag();
        this.value = value.trim();
    }

    public Locale getLocale() {
        return Locale.forLanguageTag(this.languageTag);
    }
}
