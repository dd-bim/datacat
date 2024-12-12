package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(AbstractRelationship.LABEL)
public abstract class AbstractRelationship extends XtdObject {

    public static final String LABEL = "Relationship";

    // /**
    //  * Returns a list of all relationships this item is on the owning side on.
    //  * 
    //  * @return A list of relationships.
    //  */
    // public abstract List<AbstractRelationship> getOwnedRelationships();
}
