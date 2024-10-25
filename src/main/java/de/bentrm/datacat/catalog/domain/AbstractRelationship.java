package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = AbstractRelationship.LABEL)
public abstract class AbstractRelationship extends XtdObject {

    public static final String LABEL = "Relationship";

    // /**
    //  * Returns a list of all relationships this item is on the owning side on.
    //  * 
    //  * @return A list of relationships.
    //  */
    // public abstract List<AbstractRelationship> getOwnedRelationships();
}
