package de.bentrm.datacat.repository;

import de.bentrm.datacat.query.FilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface GraphEntityRepository<T, ID extends Serializable> extends Repository<T, ID> {

	<S extends T> S save(S entity);

	<S extends T> Iterable<S> saveAll(Iterable<S> entities);

	Optional<T> findById(ID id);

	boolean existsById(ID id);

	Page<T> findAllById(Iterable<ID> ids, Pageable pageable);

	long count();

	Page<T> findAll(Pageable pageable);

	Page<T> findAll(FilterOptions<ID> filterOptions, Pageable pageable);

	Page<T> findAllByTerm(String term, Pageable pageable);

	void deleteById(ID id);

	void delete(T entity);

	void deleteAll(Iterable<? extends T> entities);

}
