package de.bentrm.datacat.catalog.service;

public interface Factory<T, U> {
    T create(U input);
}
