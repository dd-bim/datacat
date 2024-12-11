package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.AbstractRelationship;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.RelationshipRecordService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.repository.RelationshipToPropertyRepository;
import de.bentrm.datacat.graphql.input.ApiInputMapper;
import de.bentrm.datacat.graphql.input.CreateRelationshipInput;
import de.bentrm.datacat.graphql.input.DeleteRelationshipInput;
import de.bentrm.datacat.graphql.input.RelationshipPropertiesInput;
import de.bentrm.datacat.graphql.input.RelationshipToPropertyInput;
import de.bentrm.datacat.graphql.input.DeleteObjectRelationshipInput;
import de.bentrm.datacat.graphql.payload.DeleteObjectRelationshipPayload;
import de.bentrm.datacat.graphql.payload.CreateRelationshipPayload;
import de.bentrm.datacat.graphql.payload.DeleteRelationshipPayload;
import de.bentrm.datacat.graphql.payload.PayloadMapper;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
// import org.jetbrains.annotations.NotNull;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static de.bentrm.datacat.graphql.input.ApiInputMapper.OBJECT_MAPPER;

@Slf4j
@Component
public class RelationshipRecordMutations implements MutationFetchers {

    private final PayloadMapper PAYLOAD_MAPPER = PayloadMapper.INSTANCE;

    private final CatalogService catalogService;

    private final Map<CatalogRecordType, RelationshipRecordService<?>> mappedRelationshipServices;

    private final Map<CatalogRecordType, SimpleRecordService<?>> mappedSimpleRecordServices;

    private final RelationshipToPropertyRepository relationshipToPropertyRepository;

    public RelationshipRecordMutations(CatalogService catalogService,
            List<RelationshipRecordService<?>> relationshipRecordServices,
            List<SimpleRecordService<?>> simpleRecordServices,
            RelationshipToPropertyRepository relationshipToPropertyRepository) {
        this.catalogService = catalogService;
        this.relationshipToPropertyRepository = relationshipToPropertyRepository;

        log.trace("Initializing relationship record mutations...");
        this.mappedRelationshipServices = relationshipRecordServices.stream()
                .peek(service -> log.info("Registering {} bean for type {}.",
                        service.getClass().getSimpleName(), service.getSupportedCatalogRecordType()))
                .collect(Collectors.toMap(
                        RelationshipRecordService::getSupportedCatalogRecordType,
                        Function.identity()));

        log.trace("Initializing simple record mutations...");
        this.mappedSimpleRecordServices = simpleRecordServices.stream()
                .peek(service -> log.info("Registering {} bean for type {}.",
                        service.getClass().getSimpleName(), service.getSupportedCatalogRecordType()))
                .collect(Collectors.toMap(
                        SimpleRecordService::getSupportedCatalogRecordType,
                        Function.identity()));
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        return Map.of(
                "createRelationship", createRelationship(),
                "deleteRelationship", deleteRelationship(),
                "deleteObjectRelationship", deleteObjectRelationship());
    }

    public CatalogRecord createObjectRelationship(Map<String, Object> argument) {
            final CreateRelationshipInput input = OBJECT_MAPPER.convertValue(argument, CreateRelationshipInput.class);
            final RelationshipPropertiesInput inputProperties = input.getProperties();
            RelationshipRecordService<?> relationshipRecordService;
            if (input.getRelationshipType().getRelationProperty().equals("RelationshipToSubject")) {
                relationshipRecordService = getRelationshipService(CatalogRecordType.RelationshipToSubject);
            } else {
                relationshipRecordService = getRelationshipService(CatalogRecordType.RelationshipToProperty);
            }

            final AbstractRelationship catalogEntry = relationshipRecordService.addRecord(inputProperties.getId(), input.getFromId(),
                    input.getToIds());

            if (catalogEntry instanceof XtdRelationshipToProperty) {
                XtdRelationshipToProperty relationship = (XtdRelationshipToProperty) catalogEntry;
                relationship.setRelationshipType(inputProperties.getRelationshipToPropertyProperties().getRelationshipType());
                relationshipToPropertyRepository.save(relationship);
            }
            return catalogEntry;
    }

    protected DataFetcher<DeleteObjectRelationshipPayload> deleteObjectRelationship() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteObjectRelationshipInput input = OBJECT_MAPPER.convertValue(argument, DeleteObjectRelationshipInput.class);
            final String relationshipId = input.getRelationshipId();
            final RelationshipRecordService<?> relationshipRecordService = getRelationshipService(relationshipId);

            final AbstractRelationship deletedRelationship = relationshipRecordService.removeRecord(relationshipId);
            return PAYLOAD_MAPPER.toDeleteObjectRelationshipPayload(deletedRelationship);
        };
    }

    protected DataFetcher<CreateRelationshipPayload> createRelationship() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateRelationshipInput input = OBJECT_MAPPER.convertValue(argument,
                    CreateRelationshipInput.class);

            final CatalogRecord fromEntity = catalogService.getEntryById(input.getFromId()).orElseThrow();
            String entityClass = fromEntity.getClass().getSimpleName();
            final SimpleRecordService<?> simpleRecordService = getSimpleRecordService(entityClass);

            CatalogRecord record;
            SimpleRelationType relType = input.getRelationshipType();
            if (relType == SimpleRelationType.RelationshipToSubject
                    || relType == SimpleRelationType.RelationshipToProperty) {
                record = (CatalogRecord) createObjectRelationship(argument);
            } else if (simpleRecordService != null) {
                record = simpleRecordService.setRelatedRecords(input.getFromId(), input.getToIds(),
                        relType);
            } else {
                final RelationshipRecordService<?> relationshipRecordService = getRelationshipService(
                        input.getFromId());
                record = relationshipRecordService.setRelatedRecords(input.getFromId(), input.getToIds(),
                        relType);
            }

            return PAYLOAD_MAPPER.toCreateRelationshipPayload(record);
        };
    }

    protected DataFetcher<DeleteRelationshipPayload> deleteRelationship() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteRelationshipInput input = OBJECT_MAPPER.convertValue(argument, DeleteRelationshipInput.class);
  
            SimpleRelationType relType = input.getRelationshipType();
            if (relType == SimpleRelationType.RelationshipToSubject
                    || relType == SimpleRelationType.RelationshipToProperty) {
                        throw new IllegalArgumentException("The object relationships RelationshipToSubject and RelationshipToProperty have to be deleted with deleteObjectRelationship.");
                    }
            final CatalogRecord fromEntity = catalogService.getEntryById(input.getFromId()).orElseThrow();
            String entityClass = fromEntity.getClass().getSimpleName();
            final SimpleRecordService<?> simpleRecordService = getSimpleRecordService(entityClass);

            final CatalogRecord record = simpleRecordService.removeRelationship(input.getFromId(), input.getToId(), input.getRelationshipType());
            return PAYLOAD_MAPPER.toDeleteRelationshipPayload(record);
        };
    }

    @NotNull
    private RelationshipRecordService<?> getRelationshipService(String relationshipId) {
        // fetch relationship using base repository
        final AbstractRelationship relationship = catalogService.getRelationship(relationshipId).orElseThrow();

        // execute action using concrete service implementation
        final String className = relationship.getClass().getSimpleName();
        final String typeName = className.replace("Xtd", "");
        final CatalogRecordType relationshipType = CatalogRecordType.valueOf(typeName);
        return getRelationshipService(relationshipType);
    }

    @NotNull
    private RelationshipRecordService<?> getRelationshipService(CatalogRecordType relationshipType) {
        final RelationshipRecordService<?> relationshipRecordService = mappedRelationshipServices
                .get(relationshipType);
        if (relationshipRecordService == null)
            throw new IllegalArgumentException("Unsupported relationship type");
        return relationshipRecordService;
    }

    @NotNull
    private SimpleRecordService<?> getSimpleRecordService(String className) {
        final String typeName = className.replace("Xtd", "");
        final CatalogRecordType recordType = CatalogRecordType.valueOf(typeName);
        final SimpleRecordService<?> simpleRecordService = mappedSimpleRecordServices
                .get(recordType);
        return simpleRecordService;
    }

}
