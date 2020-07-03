package de.bentrm.datacat.repository;

import de.bentrm.datacat.service.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@NoRepositoryBean
public interface GraphEntityRepository<T> extends PagingAndSortingRepository<T, String> {

    @NotNull <S extends T> S save(@NotNull S entity);

    @NotNull
    Optional<T> findById(@NotNull String id);

    @Deprecated
    Page<T> findAllById(Iterable<String> ids, Pageable pageable);

    Page<T> findAll(Specification specification);

    void delete(@NotNull T entity);

}
