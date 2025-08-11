package de.bentrm.datacat.catalog.domain;

import org.springframework.util.Assert;

/**
 * Enumeration of all supported catalog record types.
 */
public enum CatalogRecordType {
    ExternalDocument(XtdExternalDocument.LABEL),
    Property(XtdProperty.LABEL),
    Subject(XtdSubject.LABEL),
    Unit(XtdUnit.LABEL),
    Value(XtdValue.LABEL),
    OrderedValue(XtdOrderedValue.LABEL),
    ValueList(XtdValueList.LABEL),
    RelationshipToSubject(XtdRelationshipToSubject.LABEL),
    RelationshipToProperty(XtdRelationshipToProperty.LABEL),
    RelationshipType(XtdRelationshipType.LABEL),
    Concept(XtdConcept.LABEL),
    Dimension(XtdDimension.LABEL),
    Rational(XtdRational.LABEL),
    MultiLanguageText(XtdMultiLanguageText.LABEL),
    Text(XtdText.LABEL),
    Symbol(XtdSymbol.LABEL),
    Interval(XtdInterval.LABEL),
    Dictionary(XtdDictionary.LABEL),
    QuantityKind(XtdQuantityKind.LABEL),
    Subdivision(XtdSubdivision.LABEL),
    Country(XtdCountry.LABEL),
    Object(XtdObject.LABEL),
    Language(XtdLanguage.LABEL);

    private final String label;

    CatalogRecordType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Helper method that returns the CatalogRecordType enum value given
     * an instance of a concrete record.
     *
     * @param record A catalog record instance.
     * @return The enum value.
     * @throws IllegalArgumentException if the enum has no record type matching with the catalog instance.
     */
    public static CatalogRecordType getByDomainClass(CatalogRecord record) {
        Assert.notNull(record, "record may not be null");
        String simpleName = record.getClass().getSimpleName();
        simpleName = simpleName
                .replace("Xtd", "");
        return CatalogRecordType.valueOf(simpleName);
    }
}
