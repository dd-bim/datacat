package de.bentrm.datacat.graphql;

import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;

public class Connection<T> {

    private final Collection<T> nodes;
    private final PageInfo pageInfo;
    private final Long totalElements;

    public static <T> Connection<T> of(Page<T> page) {
        Assert.notNull(page, "A page may never be null.");
        return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
    }

    public static <T> Connection<T> of(Collection<T> list) {
        Assert.notNull(list, "A collection may never be null.");
        return new Connection<T>(list, PageInfo.of(list), (long) list.size());
    }

    public static <T> Connection<T> empty(Long sized) {
        final List<T> emptyList = List.of();
        return new Connection<>(emptyList, PageInfo.of(emptyList), sized);
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

    public Long getTotalElements() {
        return totalElements;
    }
}
