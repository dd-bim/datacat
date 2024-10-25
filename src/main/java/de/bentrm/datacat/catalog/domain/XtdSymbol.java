package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdSymbol.LABEL)
public class XtdSymbol extends XtdRoot {

    public static final String LABEL = "XtdSymbol";

    // Subject used to attach a specific “context” for using the symbol of a property
    @Relationship(type = "SUBJECT")
    private XtdSubject subject;

    // Unicode symbol of the property
    @Relationship(type = "SYMBOL")
    private XtdText symbol;
}
