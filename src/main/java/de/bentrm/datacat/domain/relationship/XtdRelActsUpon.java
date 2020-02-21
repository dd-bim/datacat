package de.bentrm.datacat.domain.relationship;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdRelActsUpon.LABEL)
public class XtdRelActsUpon extends XtdRelAssociates {

    public static final String LABEL = "XtdRelActsUpon";

}
