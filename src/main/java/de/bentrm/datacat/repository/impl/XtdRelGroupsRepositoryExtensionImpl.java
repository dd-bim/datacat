package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.query.CountAllGroupedByQuery;
import de.bentrm.datacat.query.CountAllGroupingQuery;
import de.bentrm.datacat.query.FindAllGroupedByQuery;
import de.bentrm.datacat.query.FindAllGroupingQuery;
import de.bentrm.datacat.repository.XtdRelGroupsRepositoryExtension;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public class XtdRelGroupsRepositoryExtensionImpl implements XtdRelGroupsRepositoryExtension {

	@Autowired
	private Session session;

	@Override
	public Page<XtdRelGroups> findAllGroupedBy(String relatingThingId, Pageable pageable) {
		Iterable<XtdRelGroups> results = new FindAllGroupedByQuery<>(XtdRelGroups.class, session, relatingThingId, pageable).execute();
		List<XtdRelGroups> content = new ArrayList<>();
		results.forEach(content::add);
		return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllGroupedByQuery<>(XtdRelGroups.class, session, relatingThingId).execute());
	}

	@Override
	public Page<XtdRelGroups> findAllGrouping(String relatedThingId, Pageable pageable) {
		Iterable<XtdRelGroups> results = new FindAllGroupingQuery<>(XtdRelGroups.class, session, relatedThingId, pageable).execute();
		List<XtdRelGroups> content = new ArrayList<>();
		results.forEach(content::add);
		return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllGroupingQuery<>(XtdRelGroups.class, session, relatedThingId).execute());
	}
}
