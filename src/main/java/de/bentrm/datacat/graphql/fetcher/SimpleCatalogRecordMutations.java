package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.catalog.service.value.CatalogRecordProperties;
import de.bentrm.datacat.graphql.input.ApiInputMapper;
import de.bentrm.datacat.graphql.input.CatalogEntryPropertiesInput;
import de.bentrm.datacat.graphql.input.CreateEntryInput;
import de.bentrm.datacat.graphql.input.DeleteCatalogEntryInput;
import de.bentrm.datacat.graphql.payload.CreateEntryPayload;
import de.bentrm.datacat.graphql.payload.DeleteCatalogEntryPayload;
import de.bentrm.datacat.graphql.payload.PayloadMapper;
import graphql.schema.DataFetcher;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SimpleCatalogRecordMutations implements MutationFetchers {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final PayloadMapper PAYLOAD_MAPPER = PayloadMapper.INSTANCE;

    private final CatalogService catalogService;
    private final Map<CatalogRecordType, SimpleRecordService<?>> mappedCatalogRecordServices;

    public SimpleCatalogRecordMutations(CatalogService catalogService,
                                        List<SimpleRecordService<?>> relationshipCatalogRecordServices) {
        this.catalogService = catalogService;
        this.mappedCatalogRecordServices = relationshipCatalogRecordServices.stream()
                .collect(Collectors.toMap(
                        SimpleRecordService::getSupportedCatalogRecordType,
                        Function.identity()));
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        return Map.of(
                "createCatalogEntry", createCatalogEntry(),
                "deleteCatalogEntry", deleteCatalogEntry()
        );
    }

    protected DataFetcher<CreateEntryPayload> createCatalogEntry() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateEntryInput input = OBJECT_MAPPER.convertValue(argument, CreateEntryInput.class);
            final CatalogEntryPropertiesInput propertiesInput = input.getProperties();

            final CatalogRecordProperties properties = ApiInputMapper.INSTANCE.toProperties(propertiesInput);

            final SimpleRecordService<?> catalogRecordService = this.getCatalogRecordService(input.getCatalogEntryType());
            final CatalogRecord newRecord = catalogRecordService.addRecord(properties);

            if (input.getTags() != null) {
                input.getTags().forEach(tagId -> catalogService.addTag(newRecord.getId(), tagId));
            }

            catalogService.setMajorVersion(newRecord.getId(), 1);
            catalogService.setMinorVersion(newRecord.getId(), 0);


            return PAYLOAD_MAPPER.toCreateEntryPayload(newRecord);
        };
    }

    protected DataFetcher<DeleteCatalogEntryPayload> deleteCatalogEntry() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteCatalogEntryInput input = OBJECT_MAPPER.convertValue(argument, DeleteCatalogEntryInput.class);

            final String recordId = input.getCatalogEntryId();
            final SimpleRecordService<?> relationshipCatalogRecordService = this.getCatalogRecordService(recordId);

            final CatalogRecord deletedRecord = relationshipCatalogRecordService.removeRecord(recordId);
            return PAYLOAD_MAPPER.toDeleteEntryPayload(deletedRecord);
        };
    }

    @NotNull
    private SimpleRecordService<?> getCatalogRecordService(String recordId) {
        // fetch relationship using base repository
        final CatalogRecord catalogRecord = catalogService
                .getEntryById(recordId)
                .orElseThrow();

        // execute action using concrete service implementation
        final String className = catalogRecord.getClass().getSimpleName();
        final String typeName = className.replace("Xtd", "");
        final CatalogRecordType recordType = CatalogRecordType.valueOf(typeName);
        return getCatalogRecordService(recordType);
    }

    @NotNull
    private SimpleRecordService<?> getCatalogRecordService(CatalogRecordType relationshipType) {
        final SimpleRecordService<?> relationshipCatalogRecordService = mappedCatalogRecordServices
                .get(relationshipType);
        if (relationshipCatalogRecordService == null)
            throw new IllegalArgumentException("Unsupported catalog record type");
        return relationshipCatalogRecordService;
    }
}
