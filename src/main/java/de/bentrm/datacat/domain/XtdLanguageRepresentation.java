package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NodeEntity(label = XtdLanguageRepresentation.LABEL)
public abstract class XtdLanguageRepresentation extends Entity implements Comparable<XtdLanguageRepresentation> {

	public static final String TITLE = "LanguageRepresentation";
	public static final String TITLE_PLURAL = "LanguageRepresentations";
	public static final String LABEL = PREFIX + TITLE;

	@NotBlank
	private String languageName;

	@NotBlank
	private String value;

	@NotNull
	private int sortOrder;

	public XtdLanguageRepresentation() {}

	public XtdLanguageRepresentation(@NotBlank String languageName, @NotBlank String value) {
		this.setLanguageName(languageName);
		this.setValue(value);
	}

	public XtdLanguageRepresentation(@NotBlank String id, @NotBlank String languageName, @NotBlank String value) {
		this.setId(id);
		this.setLanguageName(languageName);
		this.setValue(value);
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
				.append("id", id)
				.append("languageName", languageName)
				.append("value", value)
				.append("sortOrder", sortOrder)
				.toString();
	}
}
