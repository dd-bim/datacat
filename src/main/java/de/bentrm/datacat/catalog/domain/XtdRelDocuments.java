package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelDocuments.LABEL)
public class XtdRelDocuments extends XtdRelationship {

    public static final String LABEL = "XtdRelDocuments";
    public static final String RELATIONSHIP_TYPE = "DOCUMENTS";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdExternalDocument relatingDocument;

    @Relationship(type = RELATIONSHIP_TYPE)
    private final Set<XtdRoot> relatedThings = new HashSet<>();

    public XtdExternalDocument getRelatingDocument() {
        return relatingDocument;
    }

    public void setRelatingDocument(XtdExternalDocument relatingDocument) {
        this.relatingDocument = relatingDocument;
    }

    public Set<XtdRoot> getRelatedThings() {
        return this.relatedThings;
    }
}