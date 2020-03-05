package de.bentrm.datacat.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PagingOptionsDto {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    private Integer pageSize = DEFAULT_PAGE_SIZE;

    private Integer pageNumber = DEFAULT_PAGE_NUMBER;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Pageable getPageble() {
        return PageRequest.of(pageNumber, pageSize);
    }

    public static PagingOptionsDto defaults() {
        return new PagingOptionsDto();
    }
}
