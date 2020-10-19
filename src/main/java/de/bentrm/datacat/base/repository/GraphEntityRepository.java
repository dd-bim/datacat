package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.base.specification.QuerySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface GraphEntityRepository<T> extends PagingAndSortingRepository<T, String> {

    long count(QuerySpecification specification);

    Optional<T> findById(String id, int depth);

    /**
     * TODO: There seems to be a bug in loading items with this method. Related properties aren't always fully populated.
     * @param specification
     * @return
     */
    @Deprecated
    Page<T> findAll(QuerySpecification specification);

    /**
     * Returns a list of node paths originating from nodes marked with the given label
     * and tagged with the given tag.
     * The structure can be mapped to a tree.
     *
     * @param label The label of the root nodes.
     * @param depth The maximum depth of the tree. Should be an even number.
     * @return A list of node id paths.
     */
    List<List<String>> getHierarchyPaths(List<String> label, int depth);
}
