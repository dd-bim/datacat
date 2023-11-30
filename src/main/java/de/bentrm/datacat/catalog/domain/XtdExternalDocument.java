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
public class XtdExternalDocument extends CatalogRecord {

    public static final String LABEL = "XtdExternalDocument";

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
