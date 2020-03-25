package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;

import java.util.Map;
import java.util.Optional;

public interface EntityDataFetcherProvider<T> {

    Map<String, DataFetcher> getQueryDataFetchers();
    Map<String, DataFetcher> getMutationDataFetchers();

    DataFetcher<T> add();
    DataFetcher<T> update();
    DataFetcher<Optional<T>> remove();

    DataFetcher<Optional<T>> getOne();
    DataFetcher<Connection<T>> getAll();


}
