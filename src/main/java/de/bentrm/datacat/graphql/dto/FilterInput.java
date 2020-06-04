package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.service.Specification;

import java.util.List;

public class FilterInput {
    private String query = null;
    private Specification.QueryScope queryScope;
    private List<String> idIn;
    private List<String> idNotIn;
    private Integer pageNumber = 0;
    private Integer pageSize = 10;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Specification.QueryScope getQueryScope() {
        return queryScope;
    }

    public void setQueryScope(Specification.QueryScope queryScope) {
        this.queryScope = queryScope;
    }

    public List<String> getIdIn() {
        return idIn;
    }

    public void setIdIn(List<String> idIn) {
        this.idIn = idIn;
    }

    public List<String> getIdNotIn() {
        return idNotIn;
    }

    public void setIdNotIn(List<String> idNotIn) {
        this.idNotIn = idNotIn;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
