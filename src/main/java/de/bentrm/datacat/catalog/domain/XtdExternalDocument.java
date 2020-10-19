package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdExternalDocument.LABEL)
public class XtdExternalDocument extends CatalogItem {

    public static final String TITLE = "ExternalDocument";
    public static final String TITLE_PLURAL = "ExternalDocuments";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = XtdRelDocuments.RELATIONSHIP_TYPE)
    private Set<XtdRelDocuments> documents = new HashSet<>();

    public Set<XtdRelDocuments> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<XtdRelDocuments> documents) {
        this.documents = documents;
    }
}
