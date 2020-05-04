package de.bentrm.datacat.graphql;

import java.util.List;

public class Connection<T> {

    private final List<T> nodes;
    private final PageInfo pageInfo;
    private final Long totalElements;

    public Connection(List<T> nodes, PageInfo pageInfo, Long totalElements) {
        this.nodes = nodes;
        this.pageInfo = pageInfo;
        this.totalElements = totalElements;
    }

    public List<T> getNodes() {
        return nodes;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
