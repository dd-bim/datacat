package de.bentrm.datacat.catalog.service;

import org.springframework.stereotype.Component;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RelationshipRecordServiceFactory {

    private final Map<CatalogRecordType, RelationshipRecordService<?>> mappedRelationshipRecordServices;

    public RelationshipRecordServiceFactory(List<RelationshipRecordService<?>> relationshipRecordServices) {
        this.mappedRelationshipRecordServices = relationshipRecordServices.stream()
                .collect(Collectors.toMap(
                    RelationshipRecordService::getSupportedCatalogRecordType,
                        Function.identity()));
    }

    @NotNull
    public RelationshipRecordService<?> getService(CatalogRecordType catalogEntryType) {
        final RelationshipRecordService<?> relationshipRecordService = mappedRelationshipRecordServices.get(catalogEntryType);
        if (relationshipRecordService == null) {
            throw new IllegalArgumentException("Unsupported catalog entry type: " + catalogEntryType);
        }
        return relationshipRecordService;
    }
}