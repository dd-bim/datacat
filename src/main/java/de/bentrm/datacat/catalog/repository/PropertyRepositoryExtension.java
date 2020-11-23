package de.bentrm.datacat.catalog.repository;

public interface PropertyRepositoryExtension {

    Iterable<String> findPropertyIdBySubjectId(String id);

}
