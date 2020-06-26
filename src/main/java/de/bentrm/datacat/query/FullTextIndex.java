package de.bentrm.datacat.query;

import java.util.List;

public enum FullTextIndex {
    ALL("translations", List.of("Translation"), List.of("label")),
    NAMES("names", List.of("Translation"), List.of("value")),
    DESCRIPTIONS("descriptions", List.of("Translation"), List.of("label"));

    private final String indexName;
    private final List<String> types;
    private final List<String> properties;

    FullTextIndex(String indexName, List<String> types, List<String> properties) {
        this.indexName = indexName;
        this.types = types;
        this.properties = properties;
    }

    public String getIndexName() {
        return indexName;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getProperties() {
        return properties;
    }
}
