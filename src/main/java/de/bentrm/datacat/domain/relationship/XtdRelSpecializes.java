package de.bentrm.datacat.domain.relationship;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdRelSpecializes.LABEL)
public class XtdRelSpecializes extends XtdRelAssociates {

    public static final String LABEL = "XtdRelSpecializes";

}
