package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdObject;
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
    private Set<XtdObject> relatedObjects = new HashSet<>();

    public XtdExternalDocument getRelatingDocument() {
        return relatingDocument;
    }

    public void setRelatingDocument(XtdExternalDocument relatingDocument) {
        this.relatingDocument = relatingDocument;
    }

    public Set<XtdObject> getRelatedObjects() {
        return this.relatedObjects;
    }

    public void setRelatedObjects(Set<XtdObject> relatedObjects) {
        this.relatedObjects = relatedObjects;
    }
}
