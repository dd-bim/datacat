package de.bentrm.datacat.domain;

import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdObject.LABEL)
public abstract class XtdObject extends XtdRoot {

    public static final String TITLE = "Object";
    public static final String TITLE_PLURAL = "Objects";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = "DOCUMENTS", direction = Relationship.INCOMING)
    private Set<XtdRelDocuments> documentedBy = new HashSet<>();

    @Relationship(type = "ASSOCIATES")
    private Set<XtdRelAssociates> associates = new HashSet<>();

    @Relationship(type = "ASSOCIATES", direction = Relationship.INCOMING)
    private Set<XtdRelAssociates> associatedBy = new HashSet<>();

    public Set<XtdRelDocuments> getDocumentedBy() {
        return documentedBy;
    }

    public Set<XtdRelAssociates> getAssociates() {
        return associates;
    }

    public Set<XtdRelAssociates> getAssociatedBy() {
        return associatedBy;
    }
}
