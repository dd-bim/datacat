package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdQuantityKind.LABEL)
public class XtdQuantityKind extends XtdConcept {

    public static final String LABEL = "XtdQuantityKind";

    @Relationship(type = "UNITS")
    private Set<XtdUnit> units = new HashSet<>();

    @Relationship(type = "DIMENSION")
    private XtdDimension dimension;
}
