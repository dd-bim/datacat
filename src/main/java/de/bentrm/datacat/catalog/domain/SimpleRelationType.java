package de.bentrm.datacat.catalog.domain;

/**
 * Enumeration of all supported catalog record types.
 */
public enum SimpleRelationType {
    BoundaryValues("BOUNDARY_VALUES"),
    CountryOfOrigin("COUNTRY_OF_ORIGIN"),
    Dictionary("DICTIONARY"),
    Dimension("DIMENSION"),
    Maximum("MAXIMUM"),
    Minimum("MINIMUM"),
    // OrderedValue("ORDERED_VALUE"),
    PossibleValues("POSSIBLE_VALUES"),
    Properties("PROPERTIES"),
    QuantityKinds("QUANTITY_KINDS"),
    ReferenceDocuments("REFERENCE_DOCUMENTS"),
    RelationshipToProperty("RelationshipToProperty"),
    RelationshipToSubject("RelationshipToSubject"),
    ReplacedObjects("REPLACED_OBJECTS"),
    ScopeSubjects("SCOPE_SUBJECTS"),
    SimilarTo("SIMILAR_TO"),
    Subject("SUBJECT"),
    Subdivisions("SUBDIVISIONS"),
    Symbols("SYMBOLS"),
    TargetProperties("TARGET_PROPERTIES"),
    TargetSubjects("TARGET_SUBJECTS"),
    Units("UNITS"),
    Unit("UNIT"),
    Values("VALUES");
    
    private final String relationProperty;

    SimpleRelationType(String relationProperty) {
        this.relationProperty = relationProperty;
    }

    public String getRelationProperty() {
        return relationProperty;
    }
}
