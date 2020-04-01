package de.bentrm.datacat.query;

public interface IterableQuery<T> {

	Iterable<T> execute();

}
