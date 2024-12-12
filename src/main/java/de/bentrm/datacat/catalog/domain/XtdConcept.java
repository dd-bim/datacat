package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import de.bentrm.datacat.util.LocalizationUtils;

import java.util.*;
import jakarta.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdConcept.LABEL)
public abstract class XtdConcept extends XtdObject {

    public static final String LABEL = "XtdConcept";

    // A list of instances of xtdMultiLanguageText that holds the definition of the concept in several languages.
    @ToString.Include
    @Relationship(type = "DEFINITION")
    private XtdMultiLanguageText definition;

    // A list of instances of xtdMultiLanguageText that holds examples of the concept in several languages.
    @ToString.Include
    @Relationship(type = "EXAMPLES")
    private final Set<XtdMultiLanguageText> examples = new HashSet<>();

    // Language of the creator of the concept.
    @ToString.Include
    @Relationship(type = "LANGUAGE_OF_CREATOR")
    private XtdLanguage languageOfCreator;

    // List of attached reference documents.
    @ToString.Include
    @Relationship(type = "REFERENCE_DOCUMENTS")
    private final Set<XtdExternalDocument> documentedBy = new HashSet<>();

    // A list of instances of xtdMultiLanguageText that holds descriptions of the concept in several languages.
    @ToString.Include
    @Relationship(type = "DESCRIPTIONS")
    protected final Set<XtdMultiLanguageText> descriptions = new HashSet<>();

    // Used to link similar concepts.
    @ToString.Include
    @Relationship(type = "SIMILAR_TO")
    private final Set<XtdConcept> similarTo = new HashSet<>();

    // // Visual representation of the concept through sketches, photos, videos or other multimedia objects.
    // @ToString.Include
    // @Relationship(type = "VISUALIZED")
    // private final Set<XtdVisualRepresentation> visualRepresentation = new HashSet<>();

    // Country from where the requirement for this concept originated.
    @ToString.Include
    @Relationship(type = "COUNTRY_OF_ORIGIN")
    private XtdCountry countryOfOrigin;


    public Optional<XtdText> getDescription(@NotNull List<Locale.LanguageRange> priorityList) {
        final XtdMultiLanguageText description = this.descriptions.stream().findFirst().orElse(null);
        if (description == null) {
            return Optional.empty();
        }
        final XtdText translation = LocalizationUtils.getTranslation(priorityList, description.getId());
        return Optional.ofNullable(translation);
    }

}
