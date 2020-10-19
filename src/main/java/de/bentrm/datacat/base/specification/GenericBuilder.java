package de.bentrm.datacat.base.specification;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ToString
public abstract class GenericBuilder<B extends GenericBuilder<B>> {

    protected final List<String> queries = new ArrayList<>();
    protected final Filters filters = new Filters();
    protected Integer pageNumber;
    protected Integer pageSize;

    protected GenericBuilder() {
    }

    public B idIn(final List<String> ids) {
        final Filter filter = new Filter("id", ComparisonOperator.IN, ids);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);
        return self();
    }

    public B idNotIn(final List<String> ids) {
        final Filter filter = new Filter("id", ComparisonOperator.IN, ids);
        filter.setBooleanOperator(BooleanOperator.AND);
        filter.setNegated(true);
        filters.add(filter);
        return self();
    }

    public abstract B query(final String query);

    protected Optional<String> sanitizeQueryString(String query) {
        if (query == null || query.isBlank()) return Optional.empty();
        if (query.contains("*")) return Optional.of(query.trim());

        final String[] strings = query.trim().split("\\s+");
        final String sanitizedQuery = Arrays.stream(strings)
                .map(str -> "*" + str + "*")
                .collect(Collectors.joining("|"));
        return Optional.of(sanitizedQuery);
    }

    public B created(final Instant created, ComparisonOperator operator) {
        final Filter filter = new Filter("created", operator, created);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);
        return self();
    }

    public B createdBy(final String username) {
        final Filter filter = new Filter("createdBy", ComparisonOperator.EQUALS, username);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);
        return self();
    }

    public B lastModified(final Instant lastModified, ComparisonOperator operator) {
        final Filter filter = new Filter("lastModified", operator, lastModified);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);
        return self();
    }

    public B lastModifiedBy(final String username) {
        final Filter filter = new Filter("lastModifiedBy", ComparisonOperator.EQUALS, username);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);
        return self();
    }

    public B pageNumber(int pageNumber) {
        Assert.isTrue(pageNumber >= 0, "Page number may not be negative.");
        this.pageNumber = pageNumber;
        return self();
    }

    public B pageSize(int pageSize) {
        Assert.isTrue(pageSize >= 1, "Page size may not be less then 1");
        this.pageSize = pageSize;
        return self();
    }

    protected B related(String propertyName, Object propertyValue, Filter.NestedPathSegment... segments) {
        final Filter filter = new Filter(propertyName, ComparisonOperator.EQUALS, propertyValue);
        filter.setBooleanOperator(BooleanOperator.AND);
        filter.setNestedPath(segments);
        this.filters.add(filter);
        return self();
    }

    protected B related(String propertyName, List<?> propertyValue, Filter.NestedPathSegment... segments) {
        final Filter filter = new Filter(propertyName, ComparisonOperator.IN, propertyValue);
        filter.setBooleanOperator(BooleanOperator.AND);
        filter.setNestedPath(segments);
        this.filters.add(filter);
        return self();
    }

    protected abstract B self();

    public abstract QuerySpecification build();
}
