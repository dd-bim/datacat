package de.bentrm.datacat.query;

public enum FullTextIndex {
    ALL("namesAndDescriptions"),
    NAMES("names"),
    DESCRIPTIONS("descriptions");

    private final String indexName;

    FullTextIndex(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexName() {
        return indexName;
    }
}
