package de.bentrm.datacat.domain.relationship;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdRelComposes.LABEL)
public class XtdRelComposes extends XtdRelAssociates {

    public static final String LABEL = "XtdRelComposes";

}
