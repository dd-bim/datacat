package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotNull;

@NodeEntity(label = XtdLanguageRepresentation.LABEL)
public abstract class XtdLanguageRepresentation extends UniqueEntity implements Comparable<XtdLanguageRepresentation> {

	public static final String TITLE = "LanguageRepresentation";
	public static final String TITLE_PLURAL = "LanguageRepresentations";
	public static final String LABEL = PREFIX + TITLE;

	@NotNull
	@Relationship(type = XtdLanguage.RELATIONSHIP_TYPE)
	private XtdLanguage languageName;

	@NotNull
	private int sortOrder;

	public XtdLanguage getLanguageName() {
		return languageName;
	}

	public void setLanguageName(XtdLanguage languageName) {
		this.languageName = languageName;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public int compareTo(XtdLanguageRepresentation o) {
		return Integer.compare(sortOrder, o.sortOrder);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("uniqueId", uniqueId)
				.append("languageName", languageName)
				.append("sortOrder", sortOrder)
				.toString();
	}
}
