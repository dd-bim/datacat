package de.bentrm.datacat.catalog.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.Assert;

import de.bentrm.datacat.util.LocalizationUtils;

import de.bentrm.datacat.catalog.domain.Enums.XtdStatusOfActivationEnum;
import java.util.*;
import jakarta.validation.constraints.NotNull;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdObject.LABEL)
public abstract class XtdObject extends XtdRoot {

    public static final String LABEL = "XtdObject";
    public static final String REPLACE_OBJECT_TYPE = "REPLACED_OBJECTS";

    public static final String DEFAULT_LANGUAGE_TAG = "de";

    // Allows tracking of major changes. Experts decide if a new major version number shall be applied.
    @ToString.Include
    private int majorVersion = 1;

    // Allows tracking of minor changes, e.g. new translation, changes of typos: if
    // the major version number changes, the minor version starts again at 1.
    // Experts decide if a new minor version number can be applied or if a new major
    // version is needed.
    @ToString.Include
    private int minorVersion = 0;

    // Primary use case for this property is search and lookup optimization
    // TODO: Add external full text search component to improve on this mechanic
    @CompositeProperty(prefix = "labels")
    @ToString.Include
    private Map<String, String> labels = new HashMap<>();

    // // Set of names of the object in different languages. Each object may have multiple names, and this allows for its expression in terms of synonyms. 
    // // At least a name shall be provided in international English and in the original language of its creator.
    @ToString.Include
    @Relationship(type = "NAMES")
    private Set<XtdMultiLanguageText> names = new HashSet<>();

    // Data dictionary to which the object belongs to.
    @ToString.Include
    @Relationship(type = "DICTIONARY")
    private XtdDictionary dictionary;

    // Konzept in Metadaten vorhanden
    // Date of creation of the concept.
    @ToString.Include
    private String dateOfCreation;

    // Status of the object during its life cycle.
    @ToString.Include
    private XtdStatusOfActivationEnum status;

    // URI of the object.
    @ToString.Include
    private String uri;

    // List of objects replaced by the current object.
    @ToString.Include
    @Relationship(type = XtdObject.REPLACE_OBJECT_TYPE, direction = Relationship.Direction.OUTGOING)
    private Set<XtdObject> replacedObjects = new HashSet<>();

    // Incoming relations of above relation
    @ToString.Include
    @Lazy
    @Relationship(type = XtdObject.REPLACE_OBJECT_TYPE, direction = Relationship.Direction.INCOMING)
    private Set<XtdObject> replacingObjects = new HashSet<>();

    // Sentence explaining the reason of the deprecation, which can explain how to convert values to conform to the new object.
    @ToString.Include
    @Relationship(type = "DEPRECATION_EXPLANATION")
    private XtdMultiLanguageText deprecationExplanation;

    @ToString.Include
    @Relationship(type = "COMMENTS")
    protected Set<XtdMultiLanguageText> comments = new HashSet<>();

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
    public Optional<XtdText> getName(@NotNull List<Locale.LanguageRange> priorityList) {
        final XtdMultiLanguageText name = this.names.stream().findFirst().orElse(null);
        if (name == null) {
            return Optional.empty();
        }
        final XtdText translation = LocalizationUtils.getTranslation(priorityList, name.getId());
        return Optional.ofNullable(translation);
    }

    /**
     * Returns an optional comment that satisfies the given language range priority
     * list.
     *
     * @param priorityList The priority list that will be used to select a
     *                     translation.
     * @return An optional translation of the catalog entries comment.
     */
    public Optional<XtdText> getComment(@NotNull List<Locale.LanguageRange> priorityList) {
        final XtdMultiLanguageText comment = this.comments.stream().findFirst().orElse(null);
        if (comment == null) {
            return Optional.empty();
        }
        final XtdText translation = LocalizationUtils.getTranslation(priorityList, comment.getId());    
        return Optional.ofNullable(translation);
    }
}
