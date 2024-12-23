package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;


@Slf4j
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
    private Set<XtdMultiLanguageText> examples = new HashSet<>();

    // Language of the creator of the concept.
    @ToString.Include
    @Relationship(type = "LANGUAGE_OF_CREATOR")
    private XtdLanguage languageOfCreator;

    // List of attached reference documents.
    @ToString.Include
    @Relationship(type = "REFERENCE_DOCUMENTS")
    private Set<XtdExternalDocument> referenceDocuments = new HashSet<>();

    // A list of instances of xtdMultiLanguageText that holds descriptions of the concept in several languages.
    @ToString.Include
    @Relationship(type = "DESCRIPTIONS")
    protected Set<XtdMultiLanguageText> descriptions = new HashSet<>();

    // Used to link similar concepts.
    @ToString.Include
    @Relationship(type = "SIMILAR_TO")
    private Set<XtdConcept> similarTo = new HashSet<>();

    // // Visual representation of the concept through sketches, photos, videos or other multimedia objects.
    // @ToString.Include
    // @Relationship(type = "VISUALIZED")
    // private final Set<XtdVisualRepresentation> visualRepresentation = new HashSet<>();

    // Country from where the requirement for this concept originated.
    @ToString.Include
    @Relationship(type = "COUNTRY_OF_ORIGIN")
    private XtdCountry countryOfOrigin;

}
