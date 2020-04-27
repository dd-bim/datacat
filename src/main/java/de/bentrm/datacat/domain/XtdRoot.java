package de.bentrm.datacat.domain;

import de.bentrm.datacat.domain.relationship.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@NodeEntity(label = XtdRoot.LABEL)
@PropertyQueryHint({
		"(root)<-[:IS_NAME_OF|IS_DESCRIPTION_OF|COMMENTS*0..1]-()",
		"(root)-[:ASSOCIATES]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF|COMMENTS*0..1]-()",
		"(root)-[:GROUPS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF|COMMENTS*0..1]-()",
		"(root)-[:SPECIALIZES]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF|COMMENTS*0..1]-()",
		"(root)-[:ACTS_UPON]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF|COMMENTS*0..1]-()"
})
public abstract class XtdRoot extends XtdEntity implements Commented {

	public static final String TITLE = "Root";
	public static final String TITLE_PLURAL = "Roots";
	public static final String LABEL = PREFIX + TITLE;

	private String versionId;

	private String versionDate;

	@Relationship(type = XtdDescription.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private SortedSet<XtdDescription> descriptions = new TreeSet<>();

	@Relationship(type = Comment.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private SortedSet<Comment> comments = new TreeSet<>();

	@Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE)
	private Set<XtdRelCollects> collects = new HashSet<>();

	@Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private Set<XtdRelCollects> collectedIn = new HashSet<>();

	@Relationship(type = XtdRelDocuments.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private final Set<XtdRelDocuments> documentedBy = new HashSet<>();

	@Relationship(type = XtdRelAssociates.RELATIONSHIP_TYPE)
	private final Set<XtdRelAssociates> associates = new HashSet<>();

	@Relationship(type = XtdRelAssociates.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private final Set<XtdRelAssociates> associatedBy = new HashSet<>();

	@Relationship(type = XtdRelGroups.RELATIONSHIP_TYPE)
	private final Set<XtdRelGroups> groups = new HashSet<>();

	@Relationship(type = XtdRelGroups.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private final Set<XtdRelGroups> groupedBy = new HashSet<>();

	@Relationship(type = XtdRelSpecializes.RELATIONSHIP_TYPE)
	private final Set<XtdRelSpecializes> specializes = new HashSet<>();

	@Relationship(type = XtdRelSpecializes.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
	private final Set<XtdRelSpecializes> specializedBy = new HashSet<>();

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

	public void addDescription(XtdDescription newDescription) {
		this.descriptions.add(newDescription);
	}

	public boolean removeDescription(XtdDescription description) {
		return this.descriptions.remove(description);
	}

	public SortedSet<Comment> getComments() {
		return comments;
	}

	public void setComments(SortedSet<Comment> comments) {
		this.comments = comments;
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

	public Set<XtdRelSpecializes> getSpecializes() {
		return specializes;
	}

	public Set<XtdRelSpecializes> getSpecializedBy() {
		return specializedBy;
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
