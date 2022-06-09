package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.TagService;
import de.bentrm.datacat.catalog.service.ValueRecordService;
import de.bentrm.datacat.graphql.input.*;
import de.bentrm.datacat.graphql.payload.*;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

// TODO: Partition service methods into smaller units
@Component
@Validated
public class ContentFetchers implements MutationFetchers {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String INPUT_ARGUMENT = "input";

    private final PayloadMapper payloadMapper;
    private final CatalogService catalogService;
    private final TagService tagService;
    private final ValueRecordService valueService;

    public ContentFetchers(PayloadMapper payloadMapper,
                           CatalogService catalogService,
                           TagService tagService,
                           ValueRecordService valueService) {
        this.payloadMapper = payloadMapper;
        this.catalogService = catalogService;
        this.tagService = tagService;
        this.valueService = valueService;
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.put("setVersion", setVersion());

        fetchers.put("addName", addName());
        fetchers.put("updateName", updateName());
        fetchers.put("deleteName", deleteName());

        fetchers.put("addDescription", addDescription());
        fetchers.put("updateDescription", updateDescription());
        fetchers.put("deleteDescription", deleteDescription());

        fetchers.put("addComment", addComment());
        fetchers.put("updateComment", updateComment());
        fetchers.put("deleteComment", deleteComment());

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

    protected DataFetcher<SetVersionPayload> setVersion() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final SetVersionInput input = OBJECT_MAPPER.convertValue(argument, SetVersionInput.class);
            final VersionInput version = input.getVersion();
            final CatalogItem item = catalogService.setVersion(input.getCatalogEntryId(), version.getVersionId(), version.getVersionDate());
            return payloadMapper.toSetVersionPayload(item);
        };
    }

    protected DataFetcher<AddNamePayload> addName() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddNameInput input = OBJECT_MAPPER.convertValue(argument, AddNameInput.class);
            final TranslationInput name = input.getName();
            final CatalogItem item = catalogService.addName(input.getCatalogEntryId(), name.getId(), name.getLocale(), name.getValue());
            return payloadMapper.toAddNamePayload(item);
        };
    }

    protected DataFetcher<UpdateNamePayload> updateName() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateNameInput input = OBJECT_MAPPER.convertValue(argument, UpdateNameInput.class);
            final TranslationUpdateInput name = input.getName();
            final CatalogItem item = catalogService.updateName(input.getCatalogEntryId(), name.getTranslationId(), name.getValue());
            return payloadMapper.toUpdateNamePayload(item);
        };
    }

    protected DataFetcher<DeleteNamePayload> deleteName() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteNameInput input = OBJECT_MAPPER.convertValue(argument, DeleteNameInput.class);
            final CatalogItem item = catalogService.deleteName(input.getCatalogEntryId(), input.getNameId());
            return payloadMapper.toDeleteNamePayload(item);
        };
    }

    protected DataFetcher<AddDescriptionPayload> addDescription() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddDescriptionInput input = OBJECT_MAPPER.convertValue(argument, AddDescriptionInput.class);
            final TranslationInput name = input.getDescription();
            final CatalogItem item = catalogService.addDescription(input.getCatalogEntryId(), name.getId(), name.getLocale(), name.getValue());
            return payloadMapper.toAddDescriptionPayload(item);
        };
    }

    protected DataFetcher<UpdateDescriptionPayload> updateDescription() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateDescriptionInput input = OBJECT_MAPPER.convertValue(argument, UpdateDescriptionInput.class);
            final TranslationUpdateInput name = input.getDescription();
            final CatalogItem item = catalogService.updateDescription(input.getCatalogEntryId(), name.getTranslationId(), name.getValue());
            return payloadMapper.toUpdateDescriptionPayload(item);
        };
    }

    protected DataFetcher<DeleteDescriptionPayload> deleteDescription() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteDescriptionInput input = OBJECT_MAPPER.convertValue(argument, DeleteDescriptionInput.class);
            final CatalogItem item = catalogService.deleteDescription(input.getCatalogEntryId(), input.getDescriptionId());
            return payloadMapper.toDeleteDescriptionPayload(item);
        };
    }

    protected DataFetcher<AddCommentPayload> addComment() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddCommentInput input = OBJECT_MAPPER.convertValue(argument, AddCommentInput.class);
            final TranslationInput name = input.getComment();
            final CatalogItem item = catalogService.addComment(input.getCatalogEntryId(), name.getId(), name.getLocale(), name.getValue());
            return payloadMapper.toAddCommentPayload(item);
        };
    }

    protected DataFetcher<UpdateCommentPayload> updateComment() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateCommentInput input = OBJECT_MAPPER.convertValue(argument, UpdateCommentInput.class);
            final TranslationUpdateInput name = input.getComment();
            final CatalogItem item = catalogService.updateComment(input.getCatalogEntryId(), name.getTranslationId(), name.getValue());
            return payloadMapper.toUpdateCommentPayload(item);
        };
    }

    protected DataFetcher<DeleteCommentPayload> deleteComment() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteCommentInput input = OBJECT_MAPPER.convertValue(argument, DeleteCommentInput.class);
            final CatalogItem item = catalogService.deleteComment(input.getCatalogEntryId(), input.getCommentId());
            return payloadMapper.toDeleteCommentPayload(item);
        };
    }

    protected DataFetcher<SetTolerancePayload> setTolerance() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final SetToleranceInput input = OBJECT_MAPPER.convertValue(argument, SetToleranceInput.class);
            final ToleranceInput tolerance = input.getTolerance();
            final XtdValue value = valueService.setTolerance(input.getValueId(), tolerance.getToleranceType(),
                    tolerance.getLowerTolerance(), tolerance.getUpperTolerance());
            return payloadMapper.toSetTolerancePayload(value);
        };
    }

    protected DataFetcher<UnsetTolerancePayload> unsetTolerance() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UnsetToleranceInput input = OBJECT_MAPPER.convertValue(argument, UnsetToleranceInput.class);
            final XtdValue value = valueService.unsetTolerance(input.getValueId());
            return payloadMapper.toUnsetTolerancePayload(value);

        };
    }

    protected DataFetcher<SetNominalValuePayload> setNominalValue() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final SetNominalValueInput input = OBJECT_MAPPER.convertValue(argument, SetNominalValueInput.class);
            final NominalValueInput nominalValue = input.getNominalValue();
            final XtdValue xtdValue = valueService.setNominalValue(input.getValueId(), nominalValue.getValueRole(),
                    nominalValue.getValueType(), nominalValue.getNominalValue());
            return payloadMapper.toSetNominalValuePayload(xtdValue);
        };
    }

    protected DataFetcher<UnsetNominalValuePayload> unsetNominalValue() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UnsetNominalValueInput input = OBJECT_MAPPER.convertValue(argument, UnsetNominalValueInput.class);
            final XtdValue xtdValue = valueService.unsetNominalValue(input.getValueId());
            return payloadMapper.toUnsetNominalValuePayload(xtdValue);
        };
    }

    private DataFetcher<CreateTagPayload> createTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateTagInput input = OBJECT_MAPPER.convertValue(argument, CreateTagInput.class);
            final Tag tag = catalogService.createTag(input.getTagId(), input.getName());
            return new CreateTagPayload(tag);
        };
    }

    private DataFetcher<UpdateTagPayload> updateTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateTagInput input = OBJECT_MAPPER.convertValue(argument, UpdateTagInput.class);
            final Tag tag = catalogService.updateTag(input.getTagId(), input.getName());
            return new UpdateTagPayload(tag);
        };
    }

    private DataFetcher<DeleteTagPayload> deleteTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteTagInput input = OBJECT_MAPPER.convertValue(argument, DeleteTagInput.class);
            final Tag tag = catalogService.deleteTag(input.getTagId());
            return new DeleteTagPayload(tag);
        };
    }

    private DataFetcher<AddTagPayload> addTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddTagInput input = OBJECT_MAPPER.convertValue(argument, AddTagInput.class);
            final CatalogItem catalogItem = catalogService.addTag(input.getCatalogEntryId(), input.getTagId());
            final Tag tag = tagService.findById(input.getTagId()).orElseThrow();
            return new AddTagPayload(catalogItem, tag);
        };
    }

    private DataFetcher<RemoveTagPayload> removeTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final RemoveTagInput input = OBJECT_MAPPER.convertValue(argument, RemoveTagInput.class);
            final CatalogItem catalogItem = catalogService.removeTag(input.getCatalogEntryId(), input.getTagId());
            final Tag tag = tagService.findById(input.getTagId()).orElseThrow();
            return new RemoveTagPayload(catalogItem, tag);
        };
    }
}
