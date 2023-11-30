package de.bentrm.datacat.catalog.domain;

import org.springframework.util.Assert;

/**
 * Enumeration of all supported catalog record types.
 */
public enum CatalogRecordType {
    AssignsCollections(XtdRelAssignsCollections.LABEL),
    AssignsMeasures(XtdRelAssignsMeasures.LABEL),
    AssignsProperties(XtdRelAssignsProperties.LABEL),
    AssignsUnits(XtdRelAssignsUnits.LABEL),
    AssignsValues(XtdRelAssignsValues.LABEL),
    Bag(XtdBag.LABEL),
    Classification(XtdClassification.LABEL),
    Collects(XtdRelCollects.LABEL),
    Documents(XtdRelDocuments.LABEL),
    ExternalDocument(XtdExternalDocument.LABEL),
    Measure(de.bentrm.datacat.catalog.domain.Measure.LABEL),
    Nest(XtdNest.LABEL),
    Property(XtdProperty.LABEL),
    Subject(XtdSubject.LABEL),
    Unit(XtdUnit.LABEL),
    Value(XtdValue.LABEL);

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
                .replace("Xtd", "")
                .replace("Rel", "");
        return CatalogRecordType.valueOf(simpleName);
    }
}
