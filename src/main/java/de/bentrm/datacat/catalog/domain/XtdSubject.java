package de.bentrm.datacat.catalog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;

@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdSubject.LABEL)
public class XtdSubject extends XtdObject {
    public static final String LABEL = "XtdSubject";
}
