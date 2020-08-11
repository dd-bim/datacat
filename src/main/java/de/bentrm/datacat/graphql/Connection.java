package de.bentrm.datacat.graphql;

import org.springframework.data.domain.Page;

import java.util.Collection;

public class Connection<T> {

    private final Collection<T> nodes;
    private final PageInfo pageInfo;
    private final Long totalElements;

    public static <T> Connection<T> of(Page<T> page) {
        return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
    }

    public static <T> Connection<T> of(Collection<T> list) {
        return new Connection<T>(list, PageInfo.of(list), (long) list.size());
    }

    public static <T> Connection<T> empty(long sized) {
        return new Connection<>(null, null, sized);
    }

    public Connection(Collection<T> nodes, PageInfo pageInfo, Long totalElements) {
        this.nodes = nodes;
        this.pageInfo = pageInfo;
        this.totalElements = totalElements;
    }

    public Collection<T> getNodes() {
        return nodes;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
