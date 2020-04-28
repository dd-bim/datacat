package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.query.CountAllAssociatedByQuery;
import de.bentrm.datacat.query.CountAllAssociatingQuery;
import de.bentrm.datacat.query.FindAllAssociatedByQuery;
import de.bentrm.datacat.query.FindAllAssociatingQuery;
import de.bentrm.datacat.repository.RelGroupsRepositoryExtension;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public class RelGroupsRepositoryExtensionImpl implements RelGroupsRepositoryExtension {

    @Autowired
    private Session session;

    @Override
    public Page<XtdRelGroups> findAllGroupedBy(String relatingThingId, Pageable pageable) {
        Iterable<XtdRelGroups> results = new FindAllAssociatedByQuery<>(XtdRelGroups.class, session, relatingThingId, pageable).execute();
        List<XtdRelGroups> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllAssociatedByQuery<>(XtdRelGroups.class, session, relatingThingId).execute());
    }

    @Override
    public Page<XtdRelGroups> findAllGrouping(String relatedThingId, Pageable pageable) {
        Iterable<XtdRelGroups> results = new FindAllAssociatingQuery<>(XtdRelGroups.class, session, relatedThingId, pageable).execute();
        List<XtdRelGroups> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllAssociatingQuery<>(XtdRelGroups.class, session, relatedThingId).execute());
    }
}
