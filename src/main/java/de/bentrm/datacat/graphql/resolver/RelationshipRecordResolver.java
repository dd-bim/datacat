package de.bentrm.datacat.graphql.resolver;

import org.springframework.stereotype.Component;

@Component
public class RelationshipRecordResolver extends XtdRelationshipResolver {

    @Override
    public String getTypeName() {
        return "RelationshipRecord";
    }
}
