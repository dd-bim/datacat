package de.bentrm.datacat.graphql.dto;

import java.util.ArrayList;
import java.util.List;

public class CatalogStatistics {

    private List<CatalogRecordStatistics> items = new ArrayList<>();

    public List<CatalogRecordStatistics> getItems() {
        return items;
    }

    public void setItems(List<CatalogRecordStatistics> items) {
        this.items = items;
    }
}
