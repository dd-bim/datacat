package de.bentrm.datacat.catalog.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelClassifies.LABEL)
public class XtdRelClassifies extends XtdRelationship {

	public static final String LABEL = "XtdRelClassifies";
	public static final String RELATIONSHIP_TYPE = "CLASSIFIES";

	@Relationship(type = RELATIONSHIP_TYPE)
	private final Set<XtdRoot> relatedThings = new HashSet<>();

	@Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private XtdClassification relatingClassification;

	public XtdClassification getRelatingClassification() {
		return relatingClassification;
	}

	public void setRelatingClassification(XtdClassification relatingClassification) {
		this.relatingClassification = relatingClassification;
	}

	public Set<XtdRoot> getRelatedThings() {
		return relatedThings;
	}
}
