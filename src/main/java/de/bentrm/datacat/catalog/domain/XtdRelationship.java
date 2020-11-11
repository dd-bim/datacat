package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

@NodeEntity(label = XtdRelationship.LABEL)
public class XtdRelationship extends CatalogItem {

    public static final String LABEL = "XtdRelationship";

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return List.of();
    }
}
