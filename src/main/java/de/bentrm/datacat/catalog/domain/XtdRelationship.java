package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdRelationship.LABEL)
public class XtdRelationship extends CatalogItem {

    public static final String LABEL = "XtdRelationship";

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return List.of();
    }
}
