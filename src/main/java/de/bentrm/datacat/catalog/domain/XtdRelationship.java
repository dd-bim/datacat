package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

/**
 * Abstract xtdRelationship implementation.
 *
 * This implementation deviates from the ISO 12006-3 specification
 * in that the xtdRelationship is not a sub-class of xtdRoot.
 * This means that a relationship can not be a member of a relationship
 * itself.
 */
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdRelationship.LABEL)
public abstract class XtdRelationship extends CatalogRecord {

    public static final String LABEL = "XtdRelationship";

    @Override
    public List<XtdRelationship> getOwnedRelationships() {
        return List.of();
    }
}
