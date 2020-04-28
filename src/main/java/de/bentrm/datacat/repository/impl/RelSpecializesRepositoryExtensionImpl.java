package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.relationship.XtdRelSpecializes;
import de.bentrm.datacat.query.CountAllAssociatedByQuery;
import de.bentrm.datacat.query.CountAllAssociatingQuery;
import de.bentrm.datacat.query.FindAllAssociatedByQuery;
import de.bentrm.datacat.query.FindAllAssociatingQuery;
import de.bentrm.datacat.repository.RelSpecializesRepositoryExtension;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public class RelSpecializesRepositoryExtensionImpl implements RelSpecializesRepositoryExtension {

    @Autowired
    private Session session;

    @Override
    public Page<XtdRelSpecializes> findAllSpecializedBy(String relatingThingId, Pageable pageable) {
        Iterable<XtdRelSpecializes> results = new FindAllAssociatedByQuery<>(XtdRelSpecializes.class, session, relatingThingId, pageable).execute();
        List<XtdRelSpecializes> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllAssociatedByQuery<>(XtdRelSpecializes.class, session, relatingThingId).execute());
    }

    @Override
    public Page<XtdRelSpecializes> findAllSpecializing(String relatedThingId, Pageable pageable) {
        Iterable<XtdRelSpecializes> results = new FindAllAssociatingQuery<>(XtdRelSpecializes.class, session, relatedThingId, pageable).execute();
        List<XtdRelSpecializes> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllAssociatingQuery<>(XtdRelSpecializes.class, session, relatedThingId).execute());
    }
}
