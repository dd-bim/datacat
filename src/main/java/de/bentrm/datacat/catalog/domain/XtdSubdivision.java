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
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdSubdivision.LABEL)
public class XtdSubdivision extends XtdConcept {

    public static final String LABEL = "XtdSubdivision";

    private String code;

    @Relationship(type = "SUBDIVISIONS")
    private Set<XtdSubdivision> subdivisions = new HashSet<>();
}
