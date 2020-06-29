package de.bentrm.datacat.service;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Specification {

    public enum QueryScope {
        ALL,
        NAMES,
        DESCRIPTIONS;
    }

    public static final QueryScope DEFAULT_QUERY_SCOPE = QueryScope.ALL;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    private String query;

    private QueryScope queryScope = DEFAULT_QUERY_SCOPE;

    private List<String> entityTypeIn;

    private List<String> entityTypeNotIn;

    private List<String> idIn;

    private List<String> idNotIn;

    private int pageNumber = DEFAULT_PAGE_NUMBER;

    private Integer pageSize = null;

    public static Specification unspecified() {
        return new Specification();
    }

    public Optional<String> getQuery() {
        return Optional.ofNullable(query);
    }

    public void setQuery(String query) {
        if (query == null || query.isBlank()) {
            this.query = null;
            return;
        }

        this.query = query.trim();
    }

    public QueryScope getQueryScope() {
        return this.queryScope == null ? DEFAULT_QUERY_SCOPE : this.queryScope;
    }

    public Specification setQueryScope(@NotNull QueryScope queryScope) {
        Assert.notNull(queryScope, "may not be null");
        this.queryScope = queryScope;
        return this;
    }

    public Optional<List<String>> getEntityTypeIn() {
        return Optional.ofNullable(entityTypeIn);
    }

    public Specification setEntityTypeIn(List<String> entityTypeIn) {
        Assert.notNull(entityTypeIn, "may not be null");
        Assert.noNullElements(entityTypeIn, "an entity type must be specified");

        this.entityTypeIn = this.filterInvalidIdentifiers(entityTypeIn);
        this.validateEntityTypeFilters();
        return this;
    }

    public Optional<List<String>> getEntityTypeNotIn() {
        return Optional.ofNullable(entityTypeNotIn);
    }

    public Specification setEntityTypeNotIn(List<String> entityTypeNotIn) {
        Assert.notNull(entityTypeNotIn, "may not be null");
        Assert.noNullElements(entityTypeNotIn, "an entity type must be specified");

        this.entityTypeNotIn = this.filterInvalidIdentifiers(entityTypeNotIn);
        this.validateEntityTypeFilters();
        return this;
    }

    private void validateEntityTypeFilters() {
        Set<String> intersection = this.getEntityTypeIn().orElseGet(ArrayList::new).stream()
                .distinct()
                .filter(this.getEntityTypeNotIn().orElseGet(ArrayList::new)::contains)
                .collect(Collectors.toSet());
        if (!intersection.isEmpty()) {
            throw new IllegalArgumentException("Intersection in entity type filters.");
        }
    }

    public Optional<List<String>> getIdIn() {
        return Optional.ofNullable(idIn);
    }

    public Specification setIdIn(List<String> idIn) {
        Assert.notNull(idIn, "may not be null");
        Assert.noNullElements(idIn, "an entity type must be specified");

        this.idIn = this.filterInvalidIdentifiers(idIn);
        this.validateIdFilters();
        return this;
    }

    public Optional<List<String>> getIdNotIn() {
        return Optional.ofNullable(idNotIn);
    }

    public Specification setIdNotIn(List<String> idNotIn) {
        Assert.notNull(idNotIn, "may not be null");
        Assert.noNullElements(idNotIn, "an entity type must be specified");

        this.idNotIn = this.filterInvalidIdentifiers(idNotIn);
        this.validateIdFilters();
        return this;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public Specification setPageNumber(int pageNumber) {
        if (pageNumber < 0) {
            this.pageNumber = DEFAULT_PAGE_NUMBER;
        } else {
            this.pageNumber = pageNumber;
        }
        return this;
    }

    public Optional<Pageable> getPageable() {
        if (pageSize != null) {
            return Optional.of(PageRequest.of(pageNumber, pageSize));
        }
        return Optional.empty();
    }

    public Optional<Integer> getPageSize() {
        return Optional.ofNullable(pageSize);
    }

    public Specification setPageSize(int pageSize) {
        if (pageSize < 1) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        } else {
            this.pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        }
        return this;
    }

    private void validateIdFilters() {
        Set<String> intersection = this.getIdIn().orElseGet(ArrayList::new).stream()
                .distinct()
                .filter(this.getIdNotIn().orElseGet(ArrayList::new)::contains)
                .collect(Collectors.toSet());
        if (!intersection.isEmpty()) {
            throw new IllegalArgumentException("Intersection in entity type filters.");
        }
    }

    private List<String> filterInvalidIdentifiers(@NotNull List<String> ids) {
        return ids.stream()
                .distinct()
                .filter(x -> x != null && !x.isBlank())
                .sorted()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("query", query)
                .append("queryScope", queryScope)
                .append("entityTypeIn", entityTypeIn)
                .append("entityTypeNotIn", entityTypeNotIn)
                .append("idIn", idIn)
                .append("idNotIn", idNotIn)
                .append("pageNumber", pageNumber)
                .append("pageSize", pageSize)
                .toString();
    }
}
