package de.bentrm.datacat.repository.dto;

import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Set;

@QueryResult
public class RelCollectsQueryResult {

    private XtdRelCollects relationship;
    private XtdCollection collection;
    private Set<XtdName> names;
    private Set<XtdDescription> descriptions;
    private Long itemCount;

    public XtdRelCollects getRelationship() {
        return relationship;
    }

    public XtdCollection getCollection() {
        return collection;
    }

    public Set<XtdName> getNames() {
        return names;
    }

    public Set<XtdDescription> getDescriptions() {
        return descriptions;
    }

    public Long getItemCount() {
        return itemCount;
    }
}
