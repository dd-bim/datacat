package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.query.CountSearchQuery;
import de.bentrm.datacat.query.FilterOptions;
import de.bentrm.datacat.query.SearchQuery;
import de.bentrm.datacat.repository.EntityRepositoryExtension;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityRepositoryExtensionImpl implements EntityRepositoryExtension {

    @Autowired
    private Session session;

    @Override
    public Page<XtdEntity> search(FilterOptions searchOptions, Pageable pageable) {
        Iterable<XtdEntity> results = new SearchQuery<>(XtdEntity.class, session, searchOptions, pageable).execute();
        List<XtdEntity> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountSearchQuery<>(XtdEntity.class, session, searchOptions).execute());
    }
}
