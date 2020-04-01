package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.query.*;
import de.bentrm.datacat.repository.GraphEntityRepository;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.support.SimpleNeo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
@Transactional(readOnly = true)
public class GraphEntityRepositoryBaseClass<T, ID extends Serializable>
		extends SimpleNeo4jRepository<T, ID>
		implements GraphEntityRepository<T, ID> {

	Logger logger = LoggerFactory.getLogger(GraphEntityRepositoryBaseClass.class);

	protected final Class<T> entityType;
	protected final Session session;

	public GraphEntityRepositoryBaseClass(Class<T> domainClass, Session session) {
		super(domainClass, session);
		this.entityType = domainClass;
		this.session = session;

		logger.debug("Initializing custom repository for class {}", domainClass);
	}

	@Override
	public Optional<T> findById(ID id) {
		FindByIdQuery<T, ID> query = new FindByIdQuery<>(entityType, session, id);
		return query.execute();
	}

	@Override
	public Page<T> findAllById(Iterable<ID> ids, Pageable pageable) {
		Iterable<T> results = new FindAllByIdQuery<>(entityType, session, ids, pageable).execute();
		List<T> content = new ArrayList<>();
		results.forEach(content::add);
		return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllByIdQuery<>(entityType, session, ids).execute());
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		Iterable<T> results = new FindAllQuery<>(entityType, session, pageable).execute();
		List<T> content = new ArrayList<>();
		results.forEach(content::add);
		return PageableExecutionUtils.getPage(content, pageable, this::count);
	}

	@Override
	public Page<T> findAllByTerm(String term, Pageable pageable) {
		Iterable<T> results = new FindAllByTermQuery<>(entityType, session, term, pageable).execute();
		List<T> content = new ArrayList<>();
		results.forEach(content::add);
		return PageableExecutionUtils.getPage(content, pageable, () -> new CountByTermQuery<>(entityType, session, term).execute());
	}
}
