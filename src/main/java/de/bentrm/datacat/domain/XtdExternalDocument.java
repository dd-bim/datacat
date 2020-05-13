package de.bentrm.datacat.domain;

import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdExternalDocument.LABEL)
@PropertyQueryHint("(root)<-[:IS_NAME_OF|COMMENTS*0..1]-()")
public class XtdExternalDocument extends XtdEntity {

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
