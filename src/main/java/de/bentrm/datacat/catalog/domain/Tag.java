package de.bentrm.datacat.catalog.domain;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.util.LocalizationUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;
import org.springframework.util.Assert;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A tag marks a concept with a certain client defined intent.
 * Concepts can be filtered by tags.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NodeEntity(label = "Tag")
public class Tag extends Entity {

    public final static String validScopePattern = "^(?![-:])[a-zA-Z0-9-:]+(?<![-:])$";

    /**
     * An optional, user-defined scope of this tag that can be used in client applications
     * to designate a special meaning, e.g. 'ISO:12006-3:XtdSubject'.
     *
     * Defaults to the empty String.
     */
    @NotNull
    private String scope = "";

    /**
     * The name of the tag keyed by language tag.
     */
    @NotEmpty
    @Properties
    private final Map<@NotNull String, @NotNull String> names = new HashMap<>();

    /**
     * A description of the usage of this tag keyed by language tag.
     */
    @NotNull
    @Properties
    protected final Map<@NotNull String, @NotNull String> descriptions = new HashMap<>();

    /**
     * Returns the name in the provided locale or the most specific language available.
     * Defaults to the tags id of no name is available.
     * See {@link LocalizationUtils#getLocalizedText} for more information.
     *
     * @param locales The user-preferred locales.
     * @return The localized name of the catalog item.
     */
    public @NotNull String getLocalizedName(Locale ...locales) {
        return LocalizationUtils.getLocalizedText(this.names, this.getId(), locales);
    }

    /**
     * Returns the description in the provided locale or the most specific language available.
     * See {@link LocalizationUtils#getLocalizedText} for more information.
     *
     * @param locales The user-preferred locales.
     * @return The localized description of the catalog item.
     */
    public String getLocalizedDescription(Locale ...locales) {
        return LocalizationUtils.getLocalizedText(this.descriptions, null, locales);
    }

    public void setScope(@NotNull String scope) {
        var newScope = scope != null ? scope.trim() : "";
        Assert.isTrue(newScope.matches(validScopePattern), "The tags scope must match the pattern '{}'. Whitespace is not allowed." + validScopePattern);
        this.scope = newScope;
    }
}
