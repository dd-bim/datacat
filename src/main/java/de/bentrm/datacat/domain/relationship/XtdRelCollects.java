package de.bentrm.datacat.domain.relationship;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.collection.XtdCollection;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = XtdRelCollects.LABEL)
public class XtdRelCollects extends XtdRelationship {

	public static final String LABEL = "XtdRelCollects";
	public static final String RELATIONSHIP_TYPE = "COLLECTS";

	@Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private XtdCollection relatingCollection;

	@Relationship(type = RELATIONSHIP_TYPE)
	private Set<XtdRoot> relatedThings = new HashSet<>();

	public XtdCollection getRelatingCollection() {
		return relatingCollection;
	}

	public void setRelatingCollection(XtdCollection relatingCollection) {
		this.relatingCollection = relatingCollection;
	}

	public Set<XtdRoot> getRelatedThings() {
		return relatedThings;
	}

	public void setRelatedThings(Set<XtdRoot> relatedThings) {
		this.relatedThings = relatedThings;
	}
}
