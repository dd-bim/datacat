package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.query.CountSearchQuery;
import de.bentrm.datacat.query.FilterOptions;
import de.bentrm.datacat.query.SearchQuery;
import de.bentrm.datacat.repository.CatalogItemRepositoryExtension;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public class CatalogItemRepositoryExtensionImpl implements CatalogItemRepositoryExtension {

    @Autowired
    private Session session;

    @Override
    public Page<CatalogItem> search(FilterOptions searchOptions, Pageable pageable) {
        Iterable<CatalogItem> results = new SearchQuery<>(CatalogItem.class, session, searchOptions, pageable).execute();
        List<CatalogItem> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountSearchQuery<>(CatalogItem.class, session, searchOptions).execute());
    }
}
