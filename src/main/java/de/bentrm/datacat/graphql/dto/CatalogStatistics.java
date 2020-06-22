package de.bentrm.datacat.graphql.dto;

import java.util.ArrayList;
import java.util.List;

public class CatalogStatistics {

    private List<CatalogItemStatistics> items = new ArrayList<>();

    public List<CatalogItemStatistics> getItems() {
        return items;
    }

    public void setItems(List<CatalogItemStatistics> items) {
        this.items = items;
    }
}
