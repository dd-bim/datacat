package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotBlank;

@NodeEntity(label = XtdDescription.LABEL)
public class XtdDescription extends XtdLanguageRepresentation {

	public static final String TITLE = "Description";
	public static final String TITLE_PLURAL = "Descriptions";
	public static final String LABEL = PREFIX + TITLE;
	public static final String RELATIONSHIP_TYPE = "IS_DESCRIPTION_OF";

	public XtdDescription() {}

	public XtdDescription(@NotBlank  String languageName, @NotBlank String value) {
		super(languageName, value);
	}

	public XtdDescription(@NotBlank String id, @NotBlank String languageName, @NotBlank String value) {
		super(id, languageName, value);
	}
}
