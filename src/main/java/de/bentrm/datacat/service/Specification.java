package de.bentrm.datacat.service;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    public enum EntityType {
        XtdExternalDocument,
        XtdRoot,
        XtdObject,
        XtdActivity,
        XtdActor,
        XtdClassification,
        XtdMeasureWithUnit,
        XtdProperty,
        XtdSubject,
        XtdUnit,
        XtdValue,
        XtdCollection,
        XtdBag,
        XtdNest,
        XtdRelationship,
        XtdRelActsUpon,
        XtdRelAssignsCollections,
        XtdRelAssignsMeasures,
        XtdRelAssignsProperties,
        XtdRelAssignsPropertyWithValues,
        XtdRelAssignsUnit,
        XtdRelAssignsValues,
        XtdRelAssociates,
        XtdRelClassifies,
        XtdRelCollects,
        XtdRelComposes,
        XtdRelDocuments,
        XtdRelGroups,
        XtdRelSequences,
        XtdRelSpecializes
    }

    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    private String query;

    private QueryScope queryScope;

    private List<String> entityTypeIn;

    private List<String> entityTypeNotIn;

    private List<String> idIn;

    private List<String> idNotIn;

    private int pageNumber = DEFAULT_PAGE_NUMBER;

    private int pageSize = DEFAULT_PAGE_SIZE;

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
        return this.queryScope == null ? QueryScope.NAMES : this.queryScope;
    }

    public void setQueryScope(QueryScope queryScope) {
        if (queryScope == null) {
            this.queryScope = QueryScope.NAMES;
        }
        this.queryScope = queryScope;
    }

    public Optional<List<String>> getEntityTypeIn() {
        return Optional.ofNullable(entityTypeIn);
    }

    public void setEntityTypeIn(List<String> entityTypeIn) {
        this.entityTypeIn = this.validateIdentifierString(entityTypeIn);
        this.validateEntityTypeFilters();
    }

    public Optional<List<String>> getEntityTypeNotIn() {
        return Optional.ofNullable(entityTypeNotIn);
    }

    public void setEntityTypeNotIn(List<String> entityTypeNotIn) {
        this.entityTypeNotIn = this.validateIdentifierString(entityTypeNotIn);
        this.validateEntityTypeFilters();
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

    public void setIdIn(List<String> idIn) {
        this.idIn = this.validateIdentifierString(idIn);
        this.validateIdFilters();
    }

    public Optional<List<String>> getIdNotIn() {
        return Optional.ofNullable(idNotIn);
    }

    public void setIdNotIn(List<String> idNotIn) {
        this.idNotIn = this.validateIdentifierString(idNotIn);
        this.validateIdFilters();
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

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        if (pageNumber < 0) {
            this.pageNumber = DEFAULT_PAGE_NUMBER;
        } else {
            this.pageNumber = pageNumber;
        }
    }

    public Pageable getPageable() {
        return PageRequest.of(pageNumber, pageSize);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            this.pageSize = DEFAULT_PAGE_NUMBER;
        } else {
            this.pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        }
    }

    private List<String> validateIdentifierString(List<String> ids) {
        if (ids == null) {
            return null;
        }

        final List<String> result = ids.stream()
                .distinct()
                .filter(x -> x != null && !x.isBlank())
                .sorted()
                .collect(Collectors.toUnmodifiableList());

        return result.isEmpty() ? null : result;
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
