package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdInterval.LABEL)
public class XtdInterval extends XtdRoot {

    public static final String LABEL = "XtdInterval";

    private boolean minimumIncluded;

    private boolean maximumIncluded;

    @Relationship(type = "MINIMUM")
    private XtdValueList minimum;

    @Relationship(type = "MAXIMUM")
    private XtdValueList maximum;
}
