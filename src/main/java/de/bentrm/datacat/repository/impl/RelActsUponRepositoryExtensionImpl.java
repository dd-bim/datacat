package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.relationship.XtdRelActsUpon;
import de.bentrm.datacat.query.CountAllAssociatedByQuery;
import de.bentrm.datacat.query.CountAllAssociatingQuery;
import de.bentrm.datacat.query.FindAllAssociatedByQuery;
import de.bentrm.datacat.query.FindAllAssociatingQuery;
import de.bentrm.datacat.repository.RelActsUponRepositoryExtension;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public class RelActsUponRepositoryExtensionImpl implements RelActsUponRepositoryExtension {

    @Autowired
    private Session session;

    @Override
    public Page<XtdRelActsUpon> findAllActedUponBy(String relatingThingId, Pageable pageable) {
        Iterable<XtdRelActsUpon> results = new FindAllAssociatedByQuery<>(XtdRelActsUpon.class, session, relatingThingId, pageable).execute();
        List<XtdRelActsUpon> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllAssociatedByQuery<>(XtdRelActsUpon.class, session, relatingThingId).execute());
    }

    @Override
    public Page<XtdRelActsUpon> findAllActingUpon(String relatedThingId, Pageable pageable) {
        Iterable<XtdRelActsUpon> results = new FindAllAssociatingQuery<>(XtdRelActsUpon.class, session, relatedThingId, pageable).execute();
        List<XtdRelActsUpon> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllAssociatingQuery<>(XtdRelActsUpon.class, session, relatedThingId).execute());
    }
}
