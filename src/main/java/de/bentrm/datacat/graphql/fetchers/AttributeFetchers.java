package de.bentrm.datacat.graphql.fetchers;

import graphql.schema.DataFetcher;

import java.util.Map;

public interface AttributeFetchers {

    String getTypeName();

    Map<String, DataFetcher> getAttributeFetchers();
}
