package de.bentrm.datacat.catalog.domain;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.util.LocalizationUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@NodeEntity(label = "CatalogItem")
public abstract class CatalogItem extends Entity {

    public static final String DEFAULT_LANGUAGE_TAG = "de";

    protected String versionId;

    protected String versionDate;

    // Primary use case for this property is search and lookup optimization
    // TODO: Add external full text search component to improve on this mechanic
    @Setter(AccessLevel.NONE)
    @Properties
    private final Map<String, String> labels = new HashMap<>();

    @Relationship(type = "NAMED")
    private final Set<Translation> names = new HashSet<>();

    @Relationship(type = "DESCRIBED")
    protected final Set<Translation> descriptions = new HashSet<>();

    @Relationship(type = "TAGGED")
    protected final Set<Tag> tags = new HashSet<>();

    public void setVersionId(String versionId) {
        if (versionId != null && versionId.isBlank()) {
            this.versionId = null;
        } else {
            this.versionId = versionId;
        }
    }

    public void setVersionDate(String versionDate) {
        if (versionDate != null && versionDate.isBlank()) {
            this.versionDate = null;
        } else {
            this.versionDate = versionDate;
        }
    }

    public Optional<Translation> getName(@NotNull List<Locale.LanguageRange> priorityList) {
        final Translation translation = LocalizationUtils.getTranslation(priorityList, this.names);
        return Optional.ofNullable(translation);
    }

    public Set<Translation> getNames() {
        return Set.copyOf(this.names);
    }

    public void addName(String translationId, Locale locale, String value) {
        final Translation translation = new Translation(translationId, locale, value);

        Assert.isTrue(translationId == null || this.names.stream().filter(x -> x.getId().equals(translationId)).findFirst().isEmpty(), "The id is already taken.");
        Assert.isTrue(this.names.stream().filter(x -> x.getLocale().equals(locale)).findFirst().isEmpty(), "The given locale is already present in the set of translations.");

        this.labels.put(translation.getLanguageTag(), translation.getValue());
        this.names.add(translation);
    }

    public void updateName(String translationId, String value) {
        Assert.hasText(translationId, "A valid id must be given.");
        Assert.hasText(value, "The value may not be null or blank.");

        final Translation translation = this.names.stream()
                .filter(x -> x.getId().equals(translationId.trim()))
                .findFirst()
                .orElseThrow();

        this.labels.put(translation.getLanguageTag(), translation.getValue());
        translation.setValue(value.trim());
    }

    public boolean deleteName(String translationId) {
        Assert.hasText(translationId, "A valid id must be given.");
        Assert.isTrue(this.names.size() > 1, "The only translation of an entry may not be deleted.");

        final Translation translation = this.names.stream()
                .filter(x -> x.getId().equals(translationId.trim()))
                .findFirst()
                .orElseThrow();

        this.labels.remove(translation.getLanguageTag());
        return this.names.remove(translation);
    }

    public Optional<Translation> getDescription(@NotNull List<Locale.LanguageRange> priorityList) {
        final Translation translation = LocalizationUtils.getTranslation(priorityList, this.descriptions);
        return Optional.ofNullable(translation);
    }

    public void addDescription(String translationId, Locale locale, String value) {
        final Translation translation = new Translation(translationId, locale, value);

        Assert.isTrue(translationId == null || this.descriptions.stream().filter(x -> x.getId().equals(translationId)).findFirst().isEmpty(), "The id is already taken.");
        Assert.isTrue(this.descriptions.stream().filter(x -> x.getLocale().equals(locale)).findFirst().isEmpty(), "The given locale is already present in the set of translations.");

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

    public boolean deleteDescription(String translationId) {
        Assert.hasText(translationId, "A valid id must be given.");

        final Translation translation = this.descriptions.stream()
                .filter(x -> x.getId().equals(translationId.trim()))
                .findFirst()
                .orElseThrow();

        return this.descriptions.remove(translation);
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
     * @return True if a tag has been removed.
     */
    public boolean removeTag(Tag tag) {
        Assert.notNull(tag, "tag may not be null");
        return this.tags.remove(tag);
    }
}
