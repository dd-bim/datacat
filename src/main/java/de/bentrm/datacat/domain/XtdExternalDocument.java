package de.bentrm.datacat.domain;

import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@NodeEntity(label = XtdExternalDocument.LABEL)
public class XtdExternalDocument extends NamedEntity implements Commented {

    public static final String TITLE = "ExternalDocument";
    public static final String TITLE_PLURAL = "ExternalDocuments";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = Comment.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private SortedSet<Comment> comments = new TreeSet<>();

    @Relationship(type = XtdRelDocuments.RELATIONSHIP_TYPE)
    private Set<XtdRelDocuments> documents = new HashSet<>();

    public SortedSet<Comment> getComments() {
        return comments;
    }

    public void setComments(SortedSet<Comment> comments) {
        this.comments = comments;
    }

    public Set<XtdRelDocuments> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<XtdRelDocuments> documents) {
        this.documents = documents;
    }
}
