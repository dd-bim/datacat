package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NodeEntity(label = XtdDescription.LABEL)
public class XtdDescription extends XtdLanguageRepresentation {

	public static final String TITLE = "Description";
	public static final String TITLE_PLURAL = "Descriptions";
	public static final String LABEL = PREFIX + TITLE;
	public static final String RELATIONSHIP_TYPE = "IS_DESCRIPTION_OF";

	@NotNull
	@NotBlank
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("description", description)
				.toString();
	}
}
