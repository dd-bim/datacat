package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import de.bentrm.datacat.catalog.domain.Enums.XtdDataTypeEnum;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdProperty.LABEL)
public class XtdProperty extends XtdConcept {

    public static final String LABEL = "XtdProperty";

    // Data type of the value of the property
    @ToString.Include
    private XtdDataTypeEnum dataType;

    // Pattern for the property values, the meaning of the pattern is implementation
    // dependant.
    @ToString.Include
    private String dataFormat;

    // Symbols of the property.
    // @ToString.Include
    @Relationship(type = "SYMBOLS")
    private Set<XtdSymbol> symbols = new HashSet<>();

    // Intervals of possible values for the property.
    @ToString.Include
    @Relationship(type = "BOUNDARY_VALUES")
    private Set<XtdInterval> boundaryValues = new HashSet<>();

    // Dimension of the property according to the ISO 80000 series.
    @ToString.Include
    @Relationship(type = "DIMENSION")
    private XtdDimension dimension;

    // List of the corresponding quantity kinds. All the quantity kinds shall
    // have the same dimension as the property.
    @ToString.Include
    @Relationship(type = "QUANTITY_KINDS")
    private Set<XtdQuantityKind> quantityKinds = new HashSet<>(); 

    // List of the possible values that can be provided for the property. Several
    // sets of possible values can be provided to allow providing them in different
    // languages.
    // @ToString.Include
    @Lazy
    @Relationship(type = "POSSIBLE_VALUES")
    private Set<XtdValueList> possibleValues = new HashSet<>();

    // List of units that can be attached to a value.
    // @ToString.Include
    @Lazy
    @Relationship(type = "UNITS")
    private Set<XtdUnit> units = new HashSet<>();

    // isUsedByFilters() 

    // List of properties connected to the current property. The connection can be a
    // specialization or a dependency.
    // @ToString.Include
    @Lazy
    @Relationship(type = XtdRelationshipToProperty.RELATIONSHIP_TYPE)
    private Set<XtdRelationshipToProperty> connectedProperties = new HashSet<>();

    // Incomming relations
    // @ToString.Include
    @Lazy
    @Relationship(type = XtdRelationshipToProperty.RELATIONSHIP_TYPE, direction = Relationship.Direction.INCOMING)
    private Set<XtdRelationshipToProperty> connectingProperties = new HashSet<>();

    // List of the properties attached to the subject.
    // @ToString.Include
    @Lazy
    @Relationship(type = "PROPERTIES", direction = Relationship.Direction.INCOMING)
    private Set<XtdSubject> subjects = new HashSet<>();
}
