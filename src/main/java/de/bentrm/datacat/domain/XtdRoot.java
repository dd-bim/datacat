package de.bentrm.datacat.domain;

import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@NodeEntity(label = XtdRoot.LABEL)
public abstract class XtdRoot extends NamedEntity {

	public static final String TITLE = "Root";
	public static final String TITLE_PLURAL = "Roots";
	public static final String LABEL = PREFIX + TITLE;

	private String versionId;

	private String versionDate;

	@Relationship(type = XtdDescription.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private SortedSet<XtdDescription> descriptions = new TreeSet<>();

	@Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE)
	private Set<XtdRelCollects> collects = new HashSet<>();

	@Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private Set<XtdRelCollects> collectedIn = new HashSet<>();

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getVersionDate() {
		return versionDate;
	}

	public void setVersionDate(String versionDate) {
		this.versionDate = versionDate;
	}

	public SortedSet<XtdDescription> getDescriptions() {
		return this.descriptions;
	}

	public XtdDescription findDescription(XtdDescription description) {
		for (XtdDescription next : this.descriptions) {
			if (next.equals(description)) {
				return next;
			}
		}
		return null;
	}

	public void addDescription(XtdDescription newDescription) {
		this.descriptions.add(newDescription);
	}

	public boolean removeDescription(XtdDescription description) {
		return this.descriptions.remove(description);
	}

	public Set<XtdRelCollects> getCollects() {
		return collects;
	}

	public void setCollects(Set<XtdRelCollects> collects) {
		this.collects = collects;
	}

	public Set<XtdRelCollects> getCollectedIn() {
		return collectedIn;
	}

	public void setCollectedIn(Set<XtdRelCollects> collectedIn) {
		this.collectedIn = collectedIn;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("versionId", versionId)
				.append("versionDate", versionDate)
				.toString();
	}
}
