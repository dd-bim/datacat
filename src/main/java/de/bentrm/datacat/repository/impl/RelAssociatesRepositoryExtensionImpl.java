package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.query.CountAllGroupedByQuery;
import de.bentrm.datacat.query.CountAllGroupingQuery;
import de.bentrm.datacat.query.FindAllAssociatedByQuery;
import de.bentrm.datacat.query.FindAllAssociatingQuery;
import de.bentrm.datacat.repository.RelAssociatesRepositoryExtension;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public class RelAssociatesRepositoryExtensionImpl implements RelAssociatesRepositoryExtension {

    @Autowired
    private Session session;

    @Override
    public Page<XtdRelAssociates> findAllAssociatedBy(String relatingThingId, Pageable pageable) {
        Iterable<XtdRelAssociates> results = new FindAllAssociatedByQuery<>(XtdRelAssociates.class, session, relatingThingId, pageable).execute();
        List<XtdRelAssociates> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllGroupedByQuery<>(XtdRelAssociates.class, session, relatingThingId).execute());
    }

    @Override
    public Page<XtdRelAssociates> findAllAssociating(String relatedThingId, Pageable pageable) {
        Iterable<XtdRelAssociates> results = new FindAllAssociatingQuery<>(XtdRelAssociates.class, session, relatedThingId, pageable).execute();
        List<XtdRelAssociates> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllGroupingQuery<>(XtdRelAssociates.class, session, relatedThingId).execute());
    }
}
