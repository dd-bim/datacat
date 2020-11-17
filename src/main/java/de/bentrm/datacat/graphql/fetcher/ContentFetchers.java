package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.service.*;
import de.bentrm.datacat.catalog.service.value.EntryValue;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import de.bentrm.datacat.catalog.service.value.OneToOneRelationshipValue;
import de.bentrm.datacat.graphql.input.*;
import de.bentrm.datacat.graphql.payload.*;
import graphql.schema.DataFetcher;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Component
@Validated
public class ContentFetchers implements MutationFetchers {

    private final String INPUT_ARGUMENT = "input";

    @Autowired
    private ApiInputMapper apiInputMapper;
    @Autowired
    private PayloadMapper payloadMapper;
    @Autowired
    private CatalogService catalogService;

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private BagService bagService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private ExternalDocumentService externalDocumentService;
    @Autowired
    private MeasureService measureService;
    @Autowired
    private NestService nestService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ValueService valueService;

    @Autowired
    private ActsUponService actsUponService;
    @Autowired
    private AssignsCollectionsService assignsCollectionsService;
    @Autowired
    private AssignsMeasuresRelationshipService assignsMeasuresRelationshipService;
    @Autowired
    private AssignsPropertiesService assignsPropertiesService;
    @Autowired
    private AssignsValuesRelationshipService assignsValuesRelationshipService;
    @Autowired
    private AssociatesService associatesService;
    @Autowired
    private CollectsService collectsService;
    @Autowired
    private ComposesService composesService;
    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private GroupsService groupsService;
    @Autowired
    private SequencesService sequencesService;
    @Autowired
    private SpecializesService specializesService;

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.put("createEntry", createEntry());
        fetchers.put("deleteEntry", deleteEntry());

        fetchers.put("createOneToOneRelationship", createOneToOneRelationship());
        fetchers.put("createOneToManyRelationship", createOneToManyRelationship());
        fetchers.put("createQualifiedOneToOneRelationship", createQualifiedOneToOneRelationship());
        fetchers.put("deleteRelationship", deleteRelationship());

        fetchers.put("setVersion", setVersion());

        fetchers.put("addName", addName());
        fetchers.put("updateName", updateName());
        fetchers.put("deleteName", deleteName());

        fetchers.put("addDescription", addDescription());
        fetchers.put("updateDescription", updateDescription());
        fetchers.put("deleteDescription", deleteDescription());

        fetchers.put("setTolerance", setTolerance());
        fetchers.put("unsetTolerance", unsetTolerance());
        fetchers.put("setNominalValue", setNominalValue());
        fetchers.put("unsetNominalValue", unsetNominalValue());

        return fetchers;
    }

    protected DataFetcher<CreateEntryPayload> createEntry() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateEntryInput input = apiInputMapper.toCreateEntryInput(argument);
            final EntryValue value = apiInputMapper.toEntryValue(input.getProperties());

            var item = switch (input.getEntryType()) {
                case Activity -> activityService.create(value);
                case Actor -> actorService.create(value);
                case Bag -> bagService.create(value);
                case Classification -> classificationService.create(value);
                case ExternalDocument -> externalDocumentService.create(value);
                case Measure -> measureService.create(value);
                case Nest -> nestService.create(value);
                case Property -> propertyService.create(value);
                case Subject -> subjectService.create(value);
                case Unit -> unitService.create(value);
                case Value -> valueService.create(value);
            };

            if (input.getTags() != null) {
                input.getTags().forEach(tagId -> catalogService.tag(item.getId(), tagId));
            }

            return payloadMapper.toCreateEntryPayload(item);
        };
    }

    protected DataFetcher<DeleteEntryPayload> deleteEntry() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteEntryInput input = apiInputMapper.toDeleteEntryInput(argument);
            final CatalogItem item = catalogService.deleteEntry(input.getId());
            return payloadMapper.toDeleteEntryPayload(item);
        };
    }

    protected DataFetcher<CreateOneToOneRelationshipPayload> createOneToOneRelationship() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateOneToOneRelationshipInput input = apiInputMapper.toCreateOneToOneRelationshipInput(argument);
            OneToOneRelationshipValue value = apiInputMapper.toOneToOneRelationshipValue(input);

            // Other sequencing relationships may be added here later
            var item = switch (input.getRelationshipType()) {
                case Sequences -> sequencesService.create(value);
            };

            return payloadMapper.toCreateOneToOneRelationshipPayload(item);
        };
    }

    protected DataFetcher<CreateOneToManyRelationshipPayload> createOneToManyRelationship() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateOneToManyRelationshipInput input = apiInputMapper.toCreateOneToManyRelationshipInput(argument);
            final OneToManyRelationshipValue value = apiInputMapper.toOneToManyRelationshipValue(input);

            var item = switch (input.getRelationshipType()) {
                case ActsUpon -> actsUponService.create(value);
                case AssignsCollections -> assignsCollectionsService.create(value);
                case AssignsMeasures -> assignsMeasuresRelationshipService.create(value);
                case AssignsProperties -> assignsPropertiesService.create(value);
                case AssignsValues -> assignsValuesRelationshipService.create(value);
                case Associates -> associatesService.create(value);
                case Collects -> collectsService.create(value);
                case Composes -> composesService.create(value);
                case Groups -> groupsService.create(value);
                case Documents -> documentsService.create(value);
                case Specializes -> specializesService.create(value);
                default -> throw new IllegalStateException("Unexpected value: " + input.getRelationshipType());
            };

            return payloadMapper.toCreateOneToManyRelationshipPayload(item);
        };
    }

    protected DataFetcher createQualifiedOneToOneRelationship() {
        return environment -> {
            throw new NotImplementedException("Not yet implemented.");
        };
    }

    protected DataFetcher<DeleteRelationshipPayload> deleteRelationship() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteRelationshipInput input = apiInputMapper.toDeleteRelationshipInput(argument);
            final XtdRelationship relationship = catalogService.deleteRelationship(input.getId());
            return payloadMapper.toDeleteRelationshipPayload(relationship);
        };
    }

    protected DataFetcher<SetVersionPayload> setVersion() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final SetVersionInput input = apiInputMapper.toSetVersionInput(argument);
            final VersionInput version = input.getVersion();
            final CatalogItem item = catalogService.setVersion(input.getId(), version.getVersionId(), version.getVersionDate());
            return payloadMapper.toSetVersionPayload(item);
        };
    }

    protected DataFetcher<AddNamePayload> addName() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddNameInput input = apiInputMapper.toAddNameInput(argument);
            final TranslationInput name = input.getName();
            final CatalogItem item = catalogService.addName(input.getEntryId(), name.getId(), name.getLocale(), name.getValue());
            return payloadMapper.toAddNamePayload(item);
        };
    }

    protected DataFetcher<UpdateNamePayload> updateName() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateNameInput input = apiInputMapper.toUpdateNameInput(argument);
            final TranslationUpdateInput name = input.getName();
            final CatalogItem item = catalogService.updateName(input.getEntryId(), name.getId(), name.getValue());
            return payloadMapper.toUpdateNamePayload(item);
        };
    }

    protected DataFetcher<DeleteNamePayload> deleteName() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteNameInput input = apiInputMapper.toDeleteNameInput(argument);
            final CatalogItem item = catalogService.deleteName(input.getEntryId(), input.getNameId());
            return payloadMapper.toDeleteNamePayload(item);
        };
    }

    protected DataFetcher<AddDescriptionPayload> addDescription() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddDescriptionInput input = apiInputMapper.toAddDescriptionInput(argument);
            final TranslationInput name = input.getDescription();
            final CatalogItem item = catalogService.addDescription(input.getEntryId(), name.getId(), name.getLocale(), name.getValue());
            return payloadMapper.toAddDescriptionPayload(item);
        };
    }

    protected DataFetcher<UpdateDescriptionPayload> updateDescription() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateDescriptionInput input = apiInputMapper.toUpdateDescriptionInput(argument);
            final TranslationUpdateInput name = input.getDescription();
            final CatalogItem item = catalogService.updateDescription(input.getEntryId(), name.getId(), name.getValue());
            return payloadMapper.toUpdateDescriptionPayload(item);
        };
    }

    protected DataFetcher<DeleteDescriptionPayload> deleteDescription() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteDescriptionInput input = apiInputMapper.toDeleteDescriptionInput(argument);
            final CatalogItem item = catalogService.deleteDescription(input.getEntryId(), input.getDescriptionId());
            return payloadMapper.toDeleteDescriptionPayload(item);
        };
    }

    protected DataFetcher<SetTolerancePayload> setTolerance() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final SetToleranceInput input = apiInputMapper.toSetToleranceInput(argument);
            final ToleranceInput tolerance = input.getTolerance();
            final XtdValue value = valueService.setTolerance(input.getId(), tolerance.getToleranceType(),
                    tolerance.getLowerTolerance(), tolerance.getUpperTolerance());
            return payloadMapper.toSetTolerancePayload(value);
        };
    }

    protected DataFetcher<UnsetTolerancePayload> unsetTolerance() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UnsetToleranceInput input = apiInputMapper.toUnsetToleranceInput(argument);
            final XtdValue value = valueService.unsetTolerance(input.getId());
            return payloadMapper.toUnsetTolerancePayload(value);

        };
    }

    protected DataFetcher<SetNominalValuePayload> setNominalValue() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final SetNominalValueInput input = apiInputMapper.toSetNominalValueInput(argument);
            final NominalValueInput nominalValue = input.getNominalValue();
            final XtdValue xtdValue = valueService.setNominalValue(input.getId(), nominalValue.getValueRole(),
                    nominalValue.getValueType(), nominalValue.getNominalValue());
            return payloadMapper.toSetNominalValuePayload(xtdValue);
        };
    }

    protected DataFetcher<UnsetNominalValuePayload> unsetNominalValue() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UnsetNominalValueInput input = apiInputMapper.toUnsetNominalValueInput(argument);
            final XtdValue xtdValue = valueService.unsetNominalValue(input.getId());
            return payloadMapper.toUnsetNominalValuePayload(xtdValue);
        };
    }
}
