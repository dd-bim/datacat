package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.service.*;
import de.bentrm.datacat.catalog.service.value.CatalogEntryProperties;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import de.bentrm.datacat.catalog.service.value.OneToOneRelationshipValue;
import de.bentrm.datacat.catalog.service.value.QualifiedOneToManyRelationshipValue;
import de.bentrm.datacat.graphql.input.*;
import de.bentrm.datacat.graphql.payload.*;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Component
@Validated
public class ContentFetchers implements MutationFetchers {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String INPUT_ARGUMENT = "input";

    @Autowired
    private ApiInputMapper apiInputMapper;
    @Autowired
    private PayloadMapper payloadMapper;
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private TagService tagService;
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

    @Autowired
    private AssignsPropertyWithValuesService assignsPropertyWithValuesService;

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.put("createCatalogEntry", createCatalogEntry());
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

        fetchers.put("createTag", createTag());
        fetchers.put("updateTag", updateTag());
        fetchers.put("deleteTag", deleteTag());
        fetchers.put("addTag", addTag());
        fetchers.put("removeTag", removeTag());


        return fetchers;
    }

    protected DataFetcher<CreateEntryPayload> createCatalogEntry() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateEntryInput input = apiInputMapper.toCreateEntryInput(argument);
            final CatalogEntryProperties value = apiInputMapper.toEntryValue(input.getProperties());

            final CatalogItem catalogEntry = catalogService.createCatalogEntry(input.getCatalogEntryType(), value);

            if (input.getTags() != null) {
                input.getTags().forEach(tagId -> catalogService.addTag(catalogEntry.getId(), tagId));
            }

            return payloadMapper.toCreateEntryPayload(catalogEntry);
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
            final CreateOneToOneRelationshipInput input = OBJECT_MAPPER.convertValue(argument, CreateOneToOneRelationshipInput.class);
            OneToOneRelationshipValue value = apiInputMapper.toValue(input);

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
            final CreateOneToManyRelationshipInput input = OBJECT_MAPPER.convertValue(argument, CreateOneToManyRelationshipInput.class);
            final OneToManyRelationshipValue value = apiInputMapper.toValue(input);

            // TODO: Classifies
            var item = switch (input.getRelationshipType()) {
                case ActsUpon -> actsUponService.create(value);
                case AssignsCollections -> assignsCollectionsService.create(value);
                case AssignsMeasures -> assignsMeasuresRelationshipService.create(value);
                case AssignsProperties -> assignsPropertiesService.create(value);
                case AssignsValues -> assignsValuesRelationshipService.create(value);
                case Associates -> associatesService.create(value);
                case Collects -> collectsService.create(value);
                case Composes -> composesService.create(value);
                case Documents -> documentsService.create(value);
                case Groups -> groupsService.create(value);
                case Specializes -> specializesService.create(value);
                default -> throw new IllegalStateException("Unexpected value: " + input.getRelationshipType());
            };

            return payloadMapper.toCreateOneToManyRelationshipPayload(item);
        };
    }

    protected DataFetcher createQualifiedOneToOneRelationship() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateQualifiedOneToManyRelationshipInput input = OBJECT_MAPPER.convertValue(argument, CreateQualifiedOneToManyRelationshipInput.class);
            final QualifiedOneToManyRelationshipValue value = apiInputMapper.toValue(input);

            // Other qualified relationships may be added here later
            var item = switch (input.getRelationshipType()) {
                case AssignsPropertyWithValues -> assignsPropertyWithValuesService.create(value);
            };

            return payloadMapper.toCreateQualifiedOneToOneRelationshipPayload(item);
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

    private DataFetcher<CreateTagPayload> createTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateTagInput input = OBJECT_MAPPER.convertValue(argument, CreateTagInput.class);
            final Tag tag = catalogService.createTag(input.getId(), input.getName());
            return new CreateTagPayload(tag);
        };
    }

    private DataFetcher<UpdateTagPayload> updateTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateTagInput input = OBJECT_MAPPER.convertValue(argument, UpdateTagInput.class);
            final Tag tag = catalogService.updateTag(input.getId(), input.getName());
            return new UpdateTagPayload(tag);
        };
    }

    private DataFetcher<DeleteTagPayload> deleteTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteTagInput input = OBJECT_MAPPER.convertValue(argument, DeleteTagInput.class);
            final Tag tag = catalogService.deleteTag(input.getId());
            return new DeleteTagPayload(tag);
        };
    }

    private DataFetcher<AddTagPayload> addTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddTagInput input = OBJECT_MAPPER.convertValue(argument, AddTagInput.class);
            final CatalogItem catalogItem = catalogService.addTag(input.getEntryId(), input.getTagId());
            final Tag tag = tagService.findById(input.getTagId());
            return new AddTagPayload(catalogItem, tag);
        };
    }

    private DataFetcher<RemoveTagPayload> removeTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final RemoveTagInput input = OBJECT_MAPPER.convertValue(argument, RemoveTagInput.class);
            final CatalogItem catalogItem = catalogService.removeTag(input.getEntryId(), input.getTagId());
            final Tag tag = tagService.findById(input.getTagId());
            return new RemoveTagPayload(catalogItem, tag);
        };
    }
}
