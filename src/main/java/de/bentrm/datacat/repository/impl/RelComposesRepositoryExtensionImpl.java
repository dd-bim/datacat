package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.relationship.XtdRelComposes;
import de.bentrm.datacat.query.CountAllAssociatedByQuery;
import de.bentrm.datacat.query.CountAllAssociatingQuery;
import de.bentrm.datacat.query.FindAllAssociatedByQuery;
import de.bentrm.datacat.query.FindAllAssociatingQuery;
import de.bentrm.datacat.repository.RelComposesRepositoryExtension;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public class RelComposesRepositoryExtensionImpl implements RelComposesRepositoryExtension {

    @Autowired
    private Session session;

    @Override
    public Page<XtdRelComposes> findAllComposedBy(String relatingThingId, Pageable pageable) {
        Iterable<XtdRelComposes> results = new FindAllAssociatedByQuery<>(XtdRelComposes.class, session, relatingThingId, pageable).execute();
        List<XtdRelComposes> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllAssociatedByQuery<>(XtdRelComposes.class, session, relatingThingId).execute());
    }

    @Override
    public Page<XtdRelComposes> findAllComposing(String relatedThingId, Pageable pageable) {
        Iterable<XtdRelComposes> results = new FindAllAssociatingQuery<>(XtdRelComposes.class, session, relatedThingId, pageable).execute();
        List<XtdRelComposes> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllAssociatingQuery<>(XtdRelComposes.class, session, relatedThingId).execute());
    }
}
