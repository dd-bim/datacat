package de.bentrm.datacat.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@NodeEntity(label = "CatalogItem")
public abstract class CatalogItem extends Entity {

    public static final String DEFAULT_LANGUAGE_TAG = "de";

    // Primary use case for this property is search and lookup optimization
    // TODO: Add external full text search component to improve on this mechanic
    @Setter(AccessLevel.NONE)
    @Properties
    private final Map<String, String> labels = new HashMap<>();

    @Relationship(type = "NAMED")
    private final Set<Translation> names = new HashSet<>();

    public String getLabel() {
        return this.getLabel(DEFAULT_LANGUAGE_TAG);
    }

    public String getLabel(String languageTag) {
        return this.labels.getOrDefault(languageTag, this.id);
    }

    public Set<Translation> getNames() {
        return Set.copyOf(this.names);
    }

    public void setName(@Nullable String id, @NotBlank String languageTag, @NotEmpty List<@NotBlank String> values) {
        final Translation translation = this.names.stream()
                .filter(x -> id != null && !id.isBlank() && x.getId().equals(id))
                .peek(x -> {
                    if (!x.getLanguageCode().equals(languageTag)) {
                        throw new IllegalArgumentException("The language code of a translation may not be changed.");
                    }
                    x.setValues(values);
                })
                .findFirst()
                .orElseGet(() -> new Translation(id, languageTag, values));
        this.names.add(translation);
        this.labels.put(translation.getLanguageCode(), translation.getLabel());
    }
}
