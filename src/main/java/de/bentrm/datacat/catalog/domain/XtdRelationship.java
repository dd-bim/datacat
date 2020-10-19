package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdRelationship.LABEL)
public class XtdRelationship extends CatalogItem {

    public static final String LABEL = "XtdRelationship";

}
