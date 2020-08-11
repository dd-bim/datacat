package de.bentrm.datacat.repository;

import de.bentrm.datacat.specification.QuerySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface GraphEntityRepository<T> extends PagingAndSortingRepository<T, String> {

    long count(QuerySpecification specification);

    Page<T> findAll(QuerySpecification specification);
}
