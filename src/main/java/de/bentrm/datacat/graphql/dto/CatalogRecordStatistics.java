package de.bentrm.datacat.graphql.dto;

public class CatalogRecordStatistics {
    private String id;
    private Long count;

    public CatalogRecordStatistics(String id, Long count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
