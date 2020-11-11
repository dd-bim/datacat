package de.bentrm.datacat.catalog.domain;

import lombok.Getter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NodeEntity(label = XtdExternalDocument.LABEL)
public class XtdExternalDocument extends CatalogItem {

    public static final String TITLE = "ExternalDocument";
    public static final String TITLE_PLURAL = "ExternalDocuments";
    public static final String LABEL = PREFIX + TITLE;

    @Relationship(type = XtdRelDocuments.RELATIONSHIP_TYPE)
    private Set<XtdRelDocuments> documents = new HashSet<>();

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return Stream
                .of(documents)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
