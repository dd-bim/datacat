package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdExternalDocument.LABEL)
public class XtdExternalDocument extends XtdConcept {

    public static final String LABEL = "XtdExternalDocument";

    private String uri;

    private String author;

    private String isbn;

    private String publisher;

    private String dateOfPublication;

    // @ToString.Include
    @Relationship(type = "LANGUAGES")
    private Set<XtdLanguage> languages = new HashSet<>();

    // @ToString.Include
    @Relationship(type = "REFERENCE_DOCUMENTS", direction = Relationship.INCOMING)
    private final Set<XtdConcept> documents = new HashSet<>();

}
