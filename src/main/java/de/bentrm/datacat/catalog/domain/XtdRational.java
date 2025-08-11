package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdRational.LABEL)
public class XtdRational extends XtdRoot {

    public static final String LABEL = "XtdRational";

    private int numerator;

    private int denominator;
}
