package de.bentrm.datacat.catalog.repository;

public interface NestRepositoryExtension {

    Iterable<String> findGroupOfPropertiesIdBySubjectId(String id);

}
