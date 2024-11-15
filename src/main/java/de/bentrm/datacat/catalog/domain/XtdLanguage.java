package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdLanguage.LABEL)
public class XtdLanguage extends XtdRoot {

    public static final String LABEL = "XtdLanguage";

    private String englishName;

    private String nativeName;

    private String code;

    private List<String> comments;
}
