package de.bentrm.datacat.graphql;

import org.springframework.data.domain.Page;

import java.util.Collection;

public class PageInfo {

    private final long pageNumber;
    private final long pageSize;
    private final long pageElements;
    private final long totalPages;

    public PageInfo(long pageNumber, long pageSize, long pageElements, long totalPages) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.pageElements = pageElements;
        this.totalPages = totalPages;
    }

    public static PageInfo of(Page<?> page) {
        return new PageInfo(page.getNumber(), page.getSize(), page.getContent().size(), page.getTotalPages());
    }

    public static PageInfo of(Collection<?> content) {
        return new PageInfo(0, content.size(), content.size(), 1);
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getPageElements() {
        return pageElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public boolean isHasNext() {
        return this.pageNumber < this.totalPages;
    }

    public boolean isHasPrevious() {
        return this.pageNumber != 0;
    }

    public boolean isFirst() {
        return pageNumber == 0;
    }

    public boolean isLast() {
        return this.pageNumber == this.totalPages;
    }

}
