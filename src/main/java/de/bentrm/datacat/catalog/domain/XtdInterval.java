package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdInterval.LABEL)
public class XtdInterval extends XtdRoot {

    public static final String LABEL = "XtdInterval";

    private Boolean minimumIncluded;

    private Boolean maximumIncluded;

    @Relationship(type = "MINIMUM")
    private XtdValueList minimum;

    @Relationship(type = "MAXIMUM")
    private XtdValueList maximum;
}
