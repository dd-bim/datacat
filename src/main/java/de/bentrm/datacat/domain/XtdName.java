package de.bentrm.datacat.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = XtdName.LABEL)
public class XtdName extends XtdLanguageRepresentation {

    public static final String TITLE = "Name";
    public static final String TITLE_PLURAL = "Names";
    public static final String LABEL = PREFIX + TITLE;
    public static final String RELATIONSHIP_TYPE = "IS_NAME_OF";

    public XtdName() {}

    public XtdName(String languageName, String value) {
        super(languageName, value);
    }

    public XtdName(String id, String languageName, String value) {
        super(id, languageName, value);
    }
}
