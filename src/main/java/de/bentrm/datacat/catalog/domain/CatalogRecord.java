package de.bentrm.datacat.catalog.domain;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.util.LocalizationUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.annotation.Version;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = "CatalogRecord")
public abstract class CatalogRecord extends Entity {

    public static final String DEFAULT_LANGUAGE_TAG = "de";

    @ToString.Include
    protected String versionId;

    @ToString.Include
    protected String versionDate;

    // Allows tracking of major changes. Experts decide if a new major version number shall be applied.
    @NotNull
    @Version
    @ToString.Include
    private int majorVersion;

    // Allows tracking of minor changes, e.g. new translation, changes of typos: if
    // the major version number changes, the minor version starts again at 1.
    // Experts decide if a new minor version number can be applied or if a new major
    // version is needed.
    @NotNull
    @Version
    @ToString.Include
    private int minorVersion;

    // Primary use case for this property is search and lookup optimization
    // TODO: Add external full text search component to improve on this mechanic
    @Setter(AccessLevel.NONE)
    @Properties
    private final Map<String, String> labels = new HashMap<>();

    @ToString.Include
    @Relationship(type = "NAMED")
    private final Set<Translation> names = new HashSet<>();

    @ToString.Include
    @Relationship(type = "DESCRIBED")
    protected final Set<Translation> descriptions = new HashSet<>();

    @ToString.Include
    @Relationship(type = "COMMENTED")
    protected final Set<Translation> comments = new HashSet<>();

    @Relationship(type = "TAGGED")
    protected final Set<Tag> tags = new HashSet<>();

    /**
     * Sets the given version id defaulting to null for blank input.
     *
     * @param versionId The new version id of the catalog entry.
     */
    public void setVersionId(String versionId) {
        if (versionId != null && versionId.isBlank()) {
            this.versionId = null;
        } else {
            this.versionId = versionId;
        }
    }

    /**
     * Sets the given version date string defaulting to null for blank input.
     *
     * @param versionDate The new version date string of the catalog entry.
     */
    public void setVersionDate(String versionDate) {
        if (versionDate != null && versionDate.isBlank()) {
            this.versionDate = null;
        } else {
            this.versionDate = versionDate;
        }
    }
    // public void setMinorVersion(Integer minorVersion) {
    // Assert.notNull(minorVersion, "minorVersion may not be null");
    // this.minorVersion = minorVersion;
    // }

    // public void setMajorVersion(Integer majorVersion) {
    // Assert.notNull(majorVersion, "majorVersion may not be null");
    // this.majorVersion = majorVersion;
    // }
    public void setMinorVersion(int minorVersion) {
        Assert.notNull(minorVersion, "minorVersion may not be null");
        this.minorVersion = minorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        Assert.notNull(majorVersion, "majorVersion may not be null");
        this.majorVersion = majorVersion;
    }

    /**
     * Returns an optional name that satisfies the given language range priority
     * list.
     *
     * @param priorityList The priority list that will be used to select a
     *                     translation.
     * @return An optional translation of the catalog entries name.
     */
    public Optional<Translation> getName(@NotNull List<Locale.LanguageRange> priorityList) {
        final Translation translation = LocalizationUtils.getTranslation(priorityList, this.names);
        return Optional.ofNullable(translation);
    }

    /**
     * Adds a new name translation for the catalog entry.
     *
     * @param translationId A predetermined id for the translation value. May be
     *                      null. Must be universally unique.
     * @param locale        The locale of the new name translation. The codified
     *                      language tag of the locale must be unique to
     *                      this catalog entry.
     * @param value         The translation value.
     */
    public void addName(String translationId, Locale locale, String value) {
        final Translation translation = new Translation(translationId, locale, value);

        Assert.isTrue(
                translationId == null
                        || this.names.stream().filter(x -> x.getId().equals(translationId)).findFirst().isEmpty(),
                "The id is already taken.");
        Assert.isTrue(this.names.stream().filter(x -> x.getLocale().equals(locale)).findFirst().isEmpty(),
                "The given locale is already present in the set of translations.");

        this.names.add(translation);
    }

    public void updateName(String translationId, String value) {
        Assert.hasText(translationId, "A valid id must be given.");
        Assert.hasText(value, "The value may not be null or blank.");

        final Translation translation = this.names.stream()
                .filter(x -> x.getId().equals(translationId.trim()))
                .findFirst()
                .orElseThrow();

        translation.setValue(value.trim());
    }

    public Translation removeName(String translationId) {
        Assert.hasText(translationId, "A valid id must be given.");
        Assert.isTrue(this.names.size() > 1, "The only translation of an entry may not be deleted.");

        final Translation translation = this.names.stream()
                .filter(x -> x.getId().equals(translationId.trim()))
                .findFirst()
                .orElseThrow();

        this.names.remove(translation);
        return translation;
    }

    public Optional<Translation> getDescription(@NotNull List<Locale.LanguageRange> priorityList) {
        final Translation translation = LocalizationUtils.getTranslation(priorityList, this.descriptions);
        return Optional.ofNullable(translation);
    }

    public void addDescription(String translationId, Locale locale, String value) {
        final Translation translation = new Translation(translationId, locale, value);

        Assert.isTrue(translationId == null
                || this.descriptions.stream().filter(x -> x.getId().equals(translationId)).findFirst().isEmpty(),
                "The id is already taken.");
        Assert.isTrue(this.descriptions.stream().filter(x -> x.getLocale().equals(locale)).findFirst().isEmpty(),
                "The given locale is already present in the set of translations.");

        this.descriptions.add(translation);
    }

    public void updateDescription(String translationId, String value) {
        Assert.hasText(translationId, "A valid id must be given.");
        Assert.hasText(value, "The value may not be null or blank.");

        final Translation translation = this.descriptions.stream()
                .filter(x -> x.getId().equals(translationId.trim()))
                .findFirst()
                .orElseThrow();

        translation.setValue(value.trim());
    }

    public void deleteDescription(String translationId) {
        Assert.hasText(translationId, "A valid id must be given.");

        final Translation translation = this.descriptions.stream()
                .filter(x -> x.getId().equals(translationId.trim()))
                .findFirst()
                .orElseThrow();

        this.descriptions.remove(translation);
    }

    public Optional<Translation> getComment(@NotNull List<Locale.LanguageRange> priorityList) {
        final Translation translation = LocalizationUtils.getTranslation(priorityList, this.comments);
        return Optional.ofNullable(translation);
    }

    public void addComment(String translationId, Locale locale, String value) {
        final Translation translation = new Translation(translationId, locale, value);

        Assert.isTrue(
                translationId == null
                        || this.comments.stream().filter(x -> x.getId().equals(translationId)).findFirst().isEmpty(),
                "The id is already taken.");
        Assert.isTrue(this.comments.stream().filter(x -> x.getLocale().equals(locale)).findFirst().isEmpty(),
                "The given locale is already present in the set of translations.");

        this.comments.add(translation);
    }

    public void updateComment(String translationId, String value) {
        Assert.hasText(translationId, "A valid id must be given.");
        Assert.hasText(value, "The value may not be null or blank.");

        final Translation translation = this.comments.stream()
                .filter(x -> x.getId().equals(translationId.trim()))
                .findFirst()
                .orElseThrow();

        translation.setValue(value.trim());
    }

    public void deleteComment(String translationId) {
        Assert.hasText(translationId, "A valid id must be given.");

        final Translation translation = this.comments.stream()
                .filter(x -> x.getId().equals(translationId.trim()))
                .findFirst()
                .orElseThrow();

        this.comments.remove(translation);
    }

    /**
     * @return An immutable set of tags.
     */
    public Set<Tag> getTags() {
        return Set.copyOf(this.tags);
    }

    /**
     * Adds a tag to the collection.
     *
     * @param tag The tag to be added.
     */
    public void addTag(Tag tag) {
        Assert.notNull(tag, "tag may not be null");
        this.tags.add(tag);
    }

    /**
     * Removes a tag from the collection.
     *
     * @param tag The tag to be removed.
     */
    public void removeTag(Tag tag) {
        Assert.notNull(tag, "tag may not be null");
        this.tags.remove(tag);
    }

    /**
     * Returns a list of all relationships this item is on the owning side on.
     * 
     * @return A list of relationships.
     */
    public abstract List<XtdRelationship> getOwnedRelationships();
}
