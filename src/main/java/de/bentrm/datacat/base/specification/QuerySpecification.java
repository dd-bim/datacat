package de.bentrm.datacat.base.specification;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.neo4j.ogm.cypher.Filters;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract query implementation that can be used to construct filter configurations
 * that can be used to query the Neo4j backend.
 */
@Slf4j
@Getter
@ToString
public abstract class QuerySpecification {

    // private final Filters filters;
    private final Map<String, String> filters;
    private final Sort.Direction sortDirection;
    private final List<String> sortBy;
    private final Integer pageNumber;
    private final Integer pageSize;

    // protected QuerySpecification(final Filters filters, final Sort.Direction sortDirection, final List<String> sortBy, final Integer pageNumber, final Integer pageSize) {
    protected QuerySpecification(final Map<String, String> filters, final Sort.Direction sortDirection, final List<String> sortBy, final Integer pageNumber, final Integer pageSize) {
        this.filters = filters;
        this.sortDirection = sortDirection != null ? sortDirection : Sort.Direction.ASC;
        this.sortBy = sortBy;
        this.pageNumber = pageNumber != null ? pageNumber : 0;
        this.pageSize = pageSize;
    }

    public Optional<Pageable> getPageable() {
        if (pageSize != null) {
            if (sortBy != null) {
                final Sort sort = Sort.by(sortDirection, sortBy.toArray(String[]::new));
                return Optional.of(PageRequest.of(pageNumber, pageSize, sort));
            }
            return Optional.of(PageRequest.of(pageNumber, pageSize));
        }
        return Optional.empty();
    }
}
