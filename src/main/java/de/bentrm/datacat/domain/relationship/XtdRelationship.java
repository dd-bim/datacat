package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdRoot;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdRelationship.LABEL)
public class XtdRelationship extends XtdRoot {

    public static final String LABEL = "XtdRelationship";

}
