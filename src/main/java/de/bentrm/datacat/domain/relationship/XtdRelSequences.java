package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdActivity;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = XtdRelSequences.LABEL)
public class XtdRelSequences extends XtdRelationship {

    public static final String LABEL = "XtdRelSequences";
    public static final String RELATIONSHIP_TYPE = "SEQUENCES";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdActivity relatingActivity;

    @Relationship(type = RELATIONSHIP_TYPE)
    private XtdActivity relatedActivity;

    public XtdActivity getRelatingActivity() {
        return relatingActivity;
    }

    public XtdRelSequences setRelatingActivity(XtdActivity relatingActivity) {
        this.relatingActivity = relatingActivity;
        return this;
    }

    public XtdActivity getRelatedActivity() {
        return relatedActivity;
    }

    public XtdRelSequences setRelatedActivity(XtdActivity relatedActivity) {
        this.relatedActivity = relatedActivity;
        return this;
    }
}
