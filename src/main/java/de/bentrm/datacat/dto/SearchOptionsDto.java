package de.bentrm.datacat.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class SearchOptionsDto {

    public static int DEFAULT_PAGE_SIZE = 10;
    public static int DEFAULT_PAGE_NUMBER = 0;

    private String term;

    private Integer pageSize = DEFAULT_PAGE_SIZE;

    private Integer pageNumber = DEFAULT_PAGE_NUMBER;

    public String getTerm() {
        return term;
    }

    public boolean hasTerm() {
        return term != null && !term.isBlank();
    }

    public void setTerm(String term) {
        this.term = term;
    }

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

    public static SearchOptionsDto defaults() {
        return new SearchOptionsDto();
    }
}
