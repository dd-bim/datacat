package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.XtdRoot;

public abstract class RootRepositoryExtensionImpl<T extends XtdRoot> extends RepositoryExtensionImpl<T> {

    protected RootRepositoryExtensionImpl(Class<T> entityType, String label) {
        super(entityType, label);
    }

    @Override
    protected String getEntityPropertyAggregations() {
        return
                "[ p=(root)<-[:IS_NAME_OF|IS_DESCRIPTION_OF|COMMENTS*0..1]-() | [relationships(p), nodes(p)] ], " +
                "[ q=(root)-[:ASSOCIATES]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF|COMMENTS*0..1]-() | [relationships(q), nodes(q)] ], " +
                "[ r=(root)-[:GROUPS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF|COMMENTS*0..1]-() | [relationships(r), nodes(r)] ]";
    }
}
