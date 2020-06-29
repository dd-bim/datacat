package de.bentrm.datacat.repository;

import de.bentrm.datacat.service.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface GraphEntityRepository<T> extends Repository<T, String> {

	<S extends T> S save(S entity);

	Optional<T> findById(String id);

	@Deprecated
	Page<T> findAllById(Iterable<String> ids, Pageable pageable);

	Page<T> findAll(Specification specification);

	void delete(T entity);

}
