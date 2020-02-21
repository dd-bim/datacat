package de.bentrm.datacat.graphql;

import de.bentrm.datacat.domain.Entity;
import org.springframework.data.domain.Page;

public class PageInfo {

    long pageNumber;
    long pageSize;
    long totalElements;
    long totalPages;
    boolean hasNext;
    boolean hasPrevious;
    boolean isFirst;
    boolean isLast;

    private PageInfo() {}

    public long getPageNumber() {
        return pageNumber;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isLast() {
        return isLast;
    }

    public static PageInfo fromPage(Page<? extends Entity> page) {
        PageInfo info = new PageInfo();
        info.pageNumber = page.getNumber();
        info.pageSize = page.getSize();
        info.totalElements = page.getTotalElements();
        info.totalPages = page.getTotalPages();
        info.hasNext = page.hasNext();
        info.hasPrevious = page.hasPrevious();
        info.isFirst = page.isFirst();
        info.isLast = page.isLast();
        return info;
    }

}
