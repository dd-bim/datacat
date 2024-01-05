package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdProperty.LABEL)
public class XtdProperty extends XtdConcept {

    public static final String LABEL = "XtdProperty";

    // Data type of the value of the property
    @ToString.Include
    private XtdDataTypeEnum datatype;

    // Pattern for the property values, the meaning of the pattern is implementation
    // dependant.
    @ToString.Include
    private String dataFormat;

    // // Symbols of the property.
    // @ToString.Include
    // @Relationship(type = "SYMBOLS")
    // private final Set<XtdSymbol> symbols = new HashSet<>(); // XtdSymbol anlegen

    // Intervals of possible values for the property.
    @ToString.Include
    @Relationship(type = "BOUNDARY_VALUES")
    private final Set<XtdInterval> boundaryValues = new HashSet<>();

    // Dimension of the property according to the ISO 80000 series.
    @ToString.Include
    private XtdDimension dimension; // XtdDimension anlegen

    // // List of the corresponding quantity kinds. All the quantity kinds shall
    // have the same dimension as the property.
    // @ToString.Include
    // @Relationship(type = "QUANTITY_KINDS")
    // private final Set<XtdQuantityKind> quantityKinds = new HashSet<>(); //
    // XtdQuantityKind anlegen

    // List of the possible values that can be provided for the property. Several
    // sets of possible values can be provided to allow providing them in different
    // languages.
    @ToString.Include
    @Relationship(type = "POSSIBLE_VALUES")
    private final Set<XtdValueList> possibleValues = new HashSet<>();

    // List of units that can be attached to a value.
    @ToString.Include
    @Relationship(type = "UNITS")
    private final Set<XtdUnit> units = new HashSet<>();

    // public void isUsedByFilters() {

    // }

    // List of properties connected to the current property. The connection can be a
    // specialization or a dependency.
    @Relationship(type = XtdRelationshipToProperty.RELATIONSHIP_TYPE)
    private final Set<XtdRelationshipToProperty> connectedProperties = new HashSet<>();

    // Incomming relations
    @Relationship(type = XtdRelationshipToProperty.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelationshipToProperty> connectingProperties = new HashSet<>();

    // @Relationship(type = XtdRelAssignsProperties.RELATIONSHIP_TYPE, direction =
    // Relationship.INCOMING)
    // private final Set<XtdRelAssignsProperties> assignedTo = new HashSet<>();

    // List of the properties attached to the subject.
    @ToString.Include
    @Relationship(type = "HAS_PROPERTY", direction = Relationship.INCOMING)
    private final Set<XtdSubject> subjects = new HashSet<>();

    @Relationship(type = XtdRelAssignsMeasures.RELATIONSHIP_TYPE)
    private final Set<XtdRelAssignsMeasures> assignedMeasures = new HashSet<>();

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return Stream.of(
                super.getOwnedRelationships(),
                assignedMeasures)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

    }
}
