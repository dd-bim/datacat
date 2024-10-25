package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.HashSet;
import java.util.Set;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdQuantityKind.LABEL)
public class XtdQuantityKind extends XtdConcept {

    public static final String LABEL = "XtdQuantityKind";

    @Relationship(type = "UNITS")
    private final Set<XtdUnit> units = new HashSet<>();

    @Relationship(type = "DIMENSION")
    private XtdDimension dimension;
}
