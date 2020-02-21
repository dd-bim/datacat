package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.XtdClassification;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelClassifies.LABEL)
public class XtdRelClassifies extends XtdRelationship {

	public static final String LABEL = "XtdRelClassifies";
	public static final String RELATIONSHIP_TYPE = "CLASSIFIES";

	@Relationship(type = RELATIONSHIP_TYPE)
	private Set<XtdRoot> relatedThings = new HashSet<>();

	@Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private XtdClassification relatingClassification;

	public Set<XtdRoot> getRelatedThings() {
		return relatedThings;
	}

	public void setRelatedThings(Set<XtdRoot> relatedThings) {
		this.relatedThings = relatedThings;
	}

	public XtdClassification getRelatingClassification() {
		return relatingClassification;
	}

	public void setRelatingClassification(XtdClassification relatingClassification) {
		this.relatingClassification = relatingClassification;
	}
}
