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
public abstract class XtdRoot extends XtdEntity {

    public static final String TITLE = "Root";
    public static final String TITLE_PLURAL = "Roots";
    public static final String LABEL = PREFIX + TITLE;

    private String versionId;

    private String versionDate;

    @Relationship(type = XtdDescription.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final SortedSet<XtdDescription> descriptions = new TreeSet<>();

    @Relationship(type = XtdRelCollects.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private Set<XtdRelCollects> collectedBy = new HashSet<>();

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

    @Relationship(type = XtdRelComposes.RELATIONSHIP_TYPE)
    private final Set<XtdRelComposes> composes = new HashSet<>();

    @Relationship(type = XtdRelComposes.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelComposes> composedBy = new HashSet<>();

    @Relationship(type = XtdRelActsUpon.RELATIONSHIP_TYPE)
    private final Set<XtdRelActsUpon> actsUpon = new HashSet<>();

    @Relationship(type = XtdRelActsUpon.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelActsUpon> actedUponBy = new HashSet<>();

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

    public Set<XtdRelCollects> getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(Set<XtdRelCollects> collectedBy) {
        this.collectedBy = collectedBy;
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

    public Set<XtdRelComposes> getComposes() {
        return composes;
    }

    public Set<XtdRelComposes> getComposedBy() {
        return composedBy;
    }

    public Set<XtdRelActsUpon> getActsUpon() {
        return actsUpon;
    }

    public Set<XtdRelActsUpon> getActedUponBy() {
        return actedUponBy;
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
