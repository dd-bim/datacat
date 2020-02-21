package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.domain.XtdUnit;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelAssignsUnit.LABEL)
public class XtdRelAssignsUnit extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsUnit";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_UNIT";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private XtdMeasureWithUnit relatingMeasure;

    @Relationship(type = RELATIONSHIP_TYPE)
    private Set<XtdUnit> relatingUnits = new HashSet<>();

    public XtdMeasureWithUnit getRelatingMeasure() {
        return relatingMeasure;
    }

    public XtdRelAssignsUnit setRelatingMeasure(XtdMeasureWithUnit relatingMeasure) {
        this.relatingMeasure = relatingMeasure;
        return this;
    }

    public Set<XtdUnit> getRelatingUnits() {
        return relatingUnits;
    }

    public XtdRelAssignsUnit setRelatingUnits(Set<XtdUnit> relatingUnits) {
        this.relatingUnits = relatingUnits;
        return this;
    }
}
