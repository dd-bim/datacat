package de.bentrm.datacat.catalog.service;

import org.springframework.stereotype.Component;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SimpleRecordServiceFactory {

    private final Map<CatalogRecordType, SimpleRecordService<?>> mappedCatalogRecordServices;

    public SimpleRecordServiceFactory(List<SimpleRecordService<?>> simpleCatalogRecordServices) {
        this.mappedCatalogRecordServices = simpleCatalogRecordServices.stream()
                .collect(Collectors.toMap(
                        SimpleRecordService::getSupportedCatalogRecordType,
                        Function.identity()));
    }

    @NotNull
    public SimpleRecordService<?> getService(CatalogRecordType catalogEntryType) {
        final SimpleRecordService<?> simpleCatalogRecordService = mappedCatalogRecordServices.get(catalogEntryType);
        if (simpleCatalogRecordService == null) {
            throw new IllegalArgumentException("Unsupported catalog entry type: " + catalogEntryType);
        }
        return simpleCatalogRecordService;
    }
}