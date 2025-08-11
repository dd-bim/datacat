package de.bentrm.datacat.graphql;

import org.springframework.data.domain.Page;

import java.util.Collection;

public class PageInfo {

    private final Long pageNumber;
    private final Long pageSize;
    private final Long pageElements;
    private final Long totalPages;

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

    public Long getPageNumber() {
        return pageNumber;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public Long getPageElements() {
        return pageElements;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public Boolean isHasNext() {
        return this.pageNumber < this.totalPages;
    }

    public Boolean isHasPrevious() {
        return this.pageNumber != 0;
    }

    public Boolean isFirst() {
        return pageNumber == 0;
    }

    public Boolean isLast() {
        return this.pageNumber == this.totalPages;
    }

}
