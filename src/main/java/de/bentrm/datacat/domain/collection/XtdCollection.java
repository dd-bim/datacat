package de.bentrm.datacat.domain.collection;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdCollection.LABEL)
public abstract class XtdCollection extends XtdRoot {

    public static final String TITLE = "Collection";
    public static final String TITLE_PLURAL = "Collections";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE)
    private Set<XtdRelCollects> collects = new HashSet<>();

    public Set<XtdRelCollects> getCollects() {
        return collects;
    }

    public void setCollects(Set<XtdRelCollects> collects) {
        this.collects = collects;
    }

}
