package de.bentrm.datacat.domain;

import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdObject.LABEL)
public abstract class XtdObject extends XtdRoot {

    public static final String TITLE = "Object";
    public static final String TITLE_PLURAL = "Objects";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = XtdRelDocuments.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private Set<XtdRelDocuments> documentedBy = new HashSet<>();

    @Relationship(type = XtdRelAssociates.RELATIONSHIP_TYPE)
    private Set<XtdRelAssociates> associates = new HashSet<>();

    @Relationship(type = XtdRelAssociates.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private Set<XtdRelAssociates> associatedBy = new HashSet<>();

    @Relationship(type = XtdRelGroups.RELATIONSHIP_TYPE)
    private Set<XtdRelGroups> groups = new HashSet<>();

    @Relationship(type = XtdRelGroups.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private Set<XtdRelGroups> groupedBy = new HashSet<>();

    public Set<XtdRelDocuments> getDocumentedBy() {
        return documentedBy;
    }

    public Set<XtdRelAssociates> getAssociates() {
        return associates;
    }

    public Set<XtdRelAssociates> getAssociatedBy() {
        return associatedBy;
    }

    public Set<XtdRelGroups> getGroups() {
        return groups;
    }

    public Set<XtdRelGroups> getGroupedBy() {
        return groupedBy;
    }
}
