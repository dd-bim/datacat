package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;
import java.util.HashSet;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdCountry.LABEL)
public class XtdCountry extends XtdConcept {

    public static final String LABEL = "XtdCountry";

    private String code;

    @Relationship(type = "SUBDIVISIONS")
    private Set<XtdSubdivision> subdivisions = new HashSet<>();
}
