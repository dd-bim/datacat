package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdExternalDocument.LABEL)
public class XtdExternalDocument extends XtdConcept {

    public static final String LABEL = "XtdExternalDocument";

    private String documentUri;

    private String author;

    private String isbn;

    private String publisher;

    private String dateOfPublication;

    // @ToString.Include
    @Relationship(type = "LANGUAGES")
    private Set<XtdLanguage> languages = new HashSet<>();

    // @ToString.Include
    @Lazy
    @Relationship(type = "REFERENCE_DOCUMENTS", direction = Relationship.Direction.INCOMING)
    private Set<XtdConcept> documents = new HashSet<>();

}
