package de.bentrm.datacat.repository;

import de.bentrm.datacat.query.FilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface GraphEntityRepository<T> extends Repository<T, String> {

	<S extends T> S save(S entity);

	<S extends T> Iterable<S> saveAll(Iterable<S> entities);

	Optional<T> findById(String id);

	boolean existsById(String id);

	Page<T> findAllById(Iterable<String> ids, Pageable pageable);

	long count();

	long count(FilterOptions filterOptions);

	Page<T> findAll(Pageable pageable);

	Page<T> findAll(FilterOptions filterOptions, Pageable pageable);

	Page<T> findAllByTerm(String term, Pageable pageable);

	void deleteById(String id);

	void delete(T entity);

	void deleteAll(Iterable<? extends T> entities);

}
