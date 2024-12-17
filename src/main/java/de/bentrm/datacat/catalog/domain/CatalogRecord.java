package de.bentrm.datacat.catalog.domain;

import de.bentrm.datacat.base.domain.Entity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.Assert;

import java.util.*;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node("CatalogRecord")
public abstract class CatalogRecord extends Entity {

    // public static final String DEFAULT_LANGUAGE_TAG = "de";

    @Relationship(type = "TAGGED")
    protected Set<Tag> tags = new HashSet<>();
    
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
}
