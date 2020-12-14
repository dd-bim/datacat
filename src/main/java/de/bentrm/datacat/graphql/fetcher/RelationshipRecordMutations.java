package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.RelationshipRecordService;
import de.bentrm.datacat.catalog.service.value.RelationshipProperties;
import de.bentrm.datacat.graphql.input.ApiInputMapper;
import de.bentrm.datacat.graphql.input.CreateRelationshipInput;
import de.bentrm.datacat.graphql.input.DeleteRelationshipInput;
import de.bentrm.datacat.graphql.input.SetRelatedEntriesInput;
import de.bentrm.datacat.graphql.payload.CreateRelationshipPayload;
import de.bentrm.datacat.graphql.payload.DeleteRelationshipPayload;
import de.bentrm.datacat.graphql.payload.PayloadMapper;
import de.bentrm.datacat.graphql.payload.SetRelatedEntriesPayload;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.bentrm.datacat.graphql.input.ApiInputMapper.OBJECT_MAPPER;

@Slf4j
@Component
public class RelationshipRecordMutations implements MutationFetchers {

    private final PayloadMapper PAYLOAD_MAPPER = PayloadMapper.INSTANCE;

    private final CatalogService catalogService;

    private final Map<CatalogRecordType, RelationshipRecordService<?>> mappedRelationshipServices;

    public RelationshipRecordMutations(CatalogService catalogService,
                                       List<RelationshipRecordService<?>> relationshipRecordServices) {
        this.catalogService = catalogService;

        log.trace("Initializing relationship record mutations...");
        this.mappedRelationshipServices = relationshipRecordServices.stream()
                .peek(service -> log.info("Registering {} bean for type {}.",
                        service.getClass().getSimpleName(), service.getSupportedCatalogRecordType()))
                .collect(Collectors.toMap(
                        RelationshipRecordService::getSupportedCatalogRecordType,
                        Function.identity()));

    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        return Map.of(
                "createRelationship", createRelationship(),
                "setRelatedEntries", setRelatedEntries(),
                "deleteRelationship", deleteRelationship()
        );
    }

    protected DataFetcher<CreateRelationshipPayload> createRelationship() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateRelationshipInput input = OBJECT_MAPPER.convertValue(argument, CreateRelationshipInput.class);
            final RelationshipProperties properties = ApiInputMapper.INSTANCE.toProperties(input.getProperties());

            final RelationshipRecordService<?> relationshipRecordService = getRelationshipService(input.getRelationshipType());
            final XtdRelationship catalogEntry = relationshipRecordService.addRecord(properties, input.getFromId(), input.getToIds());
            return PAYLOAD_MAPPER.toCreateRelationshipPayload(catalogEntry);
        };
    }

    protected DataFetcher<DeleteRelationshipPayload> deleteRelationship() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteRelationshipInput input = OBJECT_MAPPER.convertValue(argument, DeleteRelationshipInput.class);
            final String relationshipId = input.getRelationshipId();
            final RelationshipRecordService<?> relationshipRecordService = getRelationshipService(relationshipId);

            final XtdRelationship deletedRelationship = relationshipRecordService.removeRecord(relationshipId);
            return PAYLOAD_MAPPER.toDeleteRelationshipPayload(deletedRelationship);
        };
    }

    protected DataFetcher<SetRelatedEntriesPayload> setRelatedEntries() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final SetRelatedEntriesInput input = OBJECT_MAPPER.convertValue(argument, SetRelatedEntriesInput.class);
            final String relationshipId = input.getRelationshipId();

            final RelationshipRecordService<?> relationshipRecordService = getRelationshipService(relationshipId);
            final XtdRelationship updatedRelationship = relationshipRecordService.setRelatedRecords(relationshipId, input.getToIds());
            return PAYLOAD_MAPPER.toSetRelatedEntriesPayload(updatedRelationship);
        };
    }

    @NotNull
    private RelationshipRecordService<?> getRelationshipService(String relationshipId) {
        // fetch relationship using base repository
        final XtdRelationship relationship = catalogService.getRelationship(relationshipId).orElseThrow();

        // execute action using concrete service implementation
        final String className = relationship.getClass().getSimpleName();
        final String typeName = className.replace("XtdRel", "");
        final CatalogRecordType relationshipType = CatalogRecordType.valueOf(typeName);
        return getRelationshipService(relationshipType);
    }

    @NotNull
    private RelationshipRecordService<?> getRelationshipService(CatalogRecordType relationshipType) {
        final RelationshipRecordService<?> relationshipRecordService = mappedRelationshipServices
                .get(relationshipType);
        if (relationshipRecordService == null) throw new IllegalArgumentException("Unsupported relationship type");
        return relationshipRecordService;
    }

}
