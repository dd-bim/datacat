package de.bentrm.datacat.domain.relationship;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdRelGroups.LABEL)
public class XtdRelGroups extends XtdRelAssociates {

    public static final String LABEL = "XtdRelGroups";

}
