package de.bentrm.datacat.base.specification;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A generic builder class that bind method invocations to the creation of
 * filter configurations. The builder is used to build @{@link QuerySpecification}s
 * and subclasses thereof.
 * @param <B> The concrete builder class.
 */
@Slf4j
@ToString
public abstract class GenericBuilder<B extends GenericBuilder<B>> {

    protected final List<String> queries = new ArrayList<>();
    protected final List<String> filters = new ArrayList<>();
    protected Integer pageNumber;
    protected Integer pageSize;

    protected GenericBuilder() {
    }

    public B idIn(final List<String> ids) {
        final String filter = "n.id IN ['" + String.join("', '", ids) + "']";
        filters.add(filter);
        return self();
    }

    public B idNotIn(final List<String> ids) {
        final String filter = "NOT n.id IN ['" + String.join("', '", ids) + "']";
        filters.add(filter);
        return self();
    }

    public abstract B query(final String query);

    protected Optional<String> sanitizeQueryString(String query) {
        if (query == null || query.isBlank()) return Optional.empty();
        if (query.contains("*")) return Optional.of(query.trim());

        final String sanitizedQuery = ".*" + query.trim() + ".*";
        return Optional.of(sanitizedQuery);
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

    protected abstract B self();

    public abstract QuerySpecification build();
}
