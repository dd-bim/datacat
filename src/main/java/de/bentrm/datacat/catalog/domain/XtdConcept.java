package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdConcept.LABEL)
public abstract class XtdConcept extends XtdObject {

    public static final String LABEL = "XtdConcept";

    // // A list of instances of xtdMultiLanguageText that holds the definition of the concept in several languages.
    // @ToString.Include
    // @Relationship(type = "DEFINED")
    // private final Set<Translation> definitions = new HashSet<>();

    // // A list of instances of xtdMultiLanguageText that holds examples of the concept in several languages.
    // @ToString.Include
    // @Relationship(type = "EXAMPLES")
    // private final Set<Translation> examples = new HashSet<>();

    // // Language of the creator of the concept.
    // @ToString.Include
    // private XtdLanguage languageOfCreator; // XtdLanguage erstellen

    // List of attached reference documents.
    @ToString.Include
    @Relationship(type = "REFERENCE_DOCUMENTS")
    private final Set<XtdExternalDocument> documentedBy = new HashSet<>();

    // in CatalogRecords bereits realisiert
    // A list of instances of xtdMultiLanguageText that holds descriptions of the concept in several languages.
    // @ToString.Include
    // @Relationship(type = "DESCRIBED")
    // protected final Set<Translation> descriptions = new HashSet<>();

    // // Used to link similar concepts.
    // @ToString.Include
    // @Relationship(type = "SIMILAR_TO")
    // private final Set<XtdConcept> similarTo = new HashSet<>();

    // // Visual representation of the concept through sketches, photos, videos or other multimedia objects.
    // @ToString.Include
    // @Relationship(type = "VISUALIZED")
    // private final Set<XtdVisualRepresentation> visualRepresentation = new HashSet<>();

    // // Country from where the requirement for this concept originated.
    // @ToString.Include
    // private XtdCountryOfOrigin countryOfOrigin;
}
