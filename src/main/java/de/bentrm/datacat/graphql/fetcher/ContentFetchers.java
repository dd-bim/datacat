package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.TagService;
import de.bentrm.datacat.catalog.service.TextRecordService;
import de.bentrm.datacat.catalog.service.ValueRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
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
    private final ObjectRecordService objectRecordService;
    private final TextRecordService textRecordService;
    private final ConceptRecordService conceptRecordService;

    public ContentFetchers(PayloadMapper payloadMapper,
                           CatalogService catalogService,
                           TagService tagService,
                           ValueRecordService valueService,
                           TextRecordService textRecordService,
                            ConceptRecordService conceptRecordService,
                           ObjectRecordService objectRecordService) {
        this.payloadMapper = payloadMapper;
        this.catalogService = catalogService;
        this.tagService = tagService;
        this.valueService = valueService;
        this.objectRecordService = objectRecordService;
        this.textRecordService = textRecordService;
        this.conceptRecordService = conceptRecordService;
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.put("addName", addName());
        fetchers.put("updateName", updateName());
        fetchers.put("deleteName", deleteName());

        fetchers.put("addDescription", addDescription());
        fetchers.put("updateDescription", updateDescription());
        fetchers.put("deleteDescription", deleteDescription());

        fetchers.put("addComment", addComment());
        fetchers.put("updateComment", updateComment());
        fetchers.put("deleteComment", deleteComment());

        fetchers.put("createTag", createTag());
        fetchers.put("updateTag", updateTag());
        fetchers.put("deleteTag", deleteTag());
        fetchers.put("addTag", addTag());
        fetchers.put("removeTag", removeTag());


        return fetchers;
    }
    
    protected DataFetcher<AddNamePayload> addName() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddNameInput input = OBJECT_MAPPER.convertValue(argument, AddNameInput.class);
            final TranslationInput name = input.getName();
            final XtdObject item = objectRecordService.addName(input.getCatalogEntryId(), name.getId(), name.getLanguageTag(), name.getValue());
            return payloadMapper.toAddNamePayload(item);
        };
    }

    protected DataFetcher<UpdateNamePayload> updateName() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateNameInput input = OBJECT_MAPPER.convertValue(argument, UpdateNameInput.class);
            final XtdText item = textRecordService.updateText(input.getNameId(), input.getValue());
            return payloadMapper.toUpdateNamePayload(item);
        };
    }

    protected DataFetcher<DeleteNamePayload> deleteName() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteNameInput input = OBJECT_MAPPER.convertValue(argument, DeleteNameInput.class);
            final XtdText item = textRecordService.deleteText(input.getNameId());
            return payloadMapper.toDeleteNamePayload(item);
        };
    }

    protected DataFetcher<AddDescriptionPayload> addDescription() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddDescriptionInput input = OBJECT_MAPPER.convertValue(argument, AddDescriptionInput.class);
            final TranslationInput name = input.getDescription();
            final XtdConcept item = conceptRecordService.addDescription(input.getCatalogEntryId(), name.getId(), name.getLanguageTag(), name.getValue());
            return payloadMapper.toAddDescriptionPayload(item);
        };
    }

    protected DataFetcher<UpdateDescriptionPayload> updateDescription() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateDescriptionInput input = OBJECT_MAPPER.convertValue(argument, UpdateDescriptionInput.class);
            final XtdText item = textRecordService.updateText(input.getDescriptionId(), input.getValue());
            return payloadMapper.toUpdateDescriptionPayload(item);
        };
    }

    protected DataFetcher<DeleteDescriptionPayload> deleteDescription() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteDescriptionInput input = OBJECT_MAPPER.convertValue(argument, DeleteDescriptionInput.class);
            final XtdText item = textRecordService.deleteText(input.getDescriptionId());
            return payloadMapper.toDeleteDescriptionPayload(item);
        };
    }

    protected DataFetcher<AddCommentPayload> addComment() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final AddCommentInput input = OBJECT_MAPPER.convertValue(argument, AddCommentInput.class);
            final TranslationInput name = input.getComment();
            final XtdObject item = objectRecordService.addComment(input.getCatalogEntryId(), name.getId(), name.getLanguageTag(), name.getValue());
            return payloadMapper.toAddCommentPayload(item);
        };
    }

    protected DataFetcher<UpdateCommentPayload> updateComment() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final UpdateCommentInput input = OBJECT_MAPPER.convertValue(argument, UpdateCommentInput.class);
            final XtdText item = textRecordService.updateText(input.getCommentId(), input.getValue());
            return payloadMapper.toUpdateCommentPayload(item);
        };
    }

    protected DataFetcher<DeleteCommentPayload> deleteComment() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteCommentInput input = OBJECT_MAPPER.convertValue(argument, DeleteCommentInput.class);
            final XtdText item = textRecordService.deleteText(input.getCommentId());
            return payloadMapper.toDeleteCommentPayload(item);
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
            final CatalogRecord catalogRecord = catalogService.addTag(input.getCatalogEntryId(), input.getTagId());
            final Tag tag = tagService.findById(input.getTagId()).orElseThrow();
            return new AddTagPayload(catalogRecord, tag);
        };
    }

    private DataFetcher<RemoveTagPayload> removeTag() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final RemoveTagInput input = OBJECT_MAPPER.convertValue(argument, RemoveTagInput.class);
            final CatalogRecord catalogRecord = catalogService.removeTag(input.getCatalogEntryId(), input.getTagId());
            final Tag tag = tagService.findById(input.getTagId()).orElseThrow();
            return new RemoveTagPayload(catalogRecord, tag);
        };
    }
    
}
