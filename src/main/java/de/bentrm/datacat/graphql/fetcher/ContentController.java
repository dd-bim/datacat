package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.TagService;
import de.bentrm.datacat.catalog.service.TextRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.graphql.input.*;
import de.bentrm.datacat.graphql.payload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@Validated
public class ContentController {

    @Autowired
    private PayloadMapper payloadMapper;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ObjectRecordService objectRecordService;

    @Autowired
    private TextRecordService textRecordService;

    @Autowired
    private ConceptRecordService conceptRecordService;

    @MutationMapping
    protected AddNamePayload addName(@Argument AddNameInput input) {
        final XtdObject item = objectRecordService.addName(input);
        return payloadMapper.toAddNamePayload(item);
    }

    // protected DataFetcher<UpdateNamePayload> updateName() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final UpdateNameInput input = OBJECT_MAPPER.convertValue(argument, UpdateNameInput.class);
    //         final XtdText item = textRecordService.updateText(input.getNameId(), input.getValue());
    //         return payloadMapper.toUpdateNamePayload(item);
    //     };
    // }

    @MutationMapping
    protected UpdateNamePayload updateName(@Argument UpdateNameInput input) {
        final XtdText item = textRecordService.updateText(input.getNameId(), input.getValue());
        return payloadMapper.toUpdateNamePayload(item);
    }

    // protected DataFetcher<DeleteNamePayload> deleteName() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final DeleteNameInput input = OBJECT_MAPPER.convertValue(argument, DeleteNameInput.class);
    //         final XtdText item = textRecordService.deleteText(input.getNameId());
    //         return payloadMapper.toDeleteNamePayload(item);
    //     };
    // }

    @MutationMapping
    protected DeleteNamePayload deleteName(@Argument DeleteNameInput input) {
        final XtdText item = textRecordService.deleteText(input.getNameId());
        return payloadMapper.toDeleteNamePayload(item);
    }

    // protected DataFetcher<AddDescriptionPayload> addDescription() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final AddDescriptionInput input = OBJECT_MAPPER.convertValue(argument, AddDescriptionInput.class);
    //         final TranslationInput name = input.getDescription();
    //         final XtdConcept item = conceptRecordService.addDescription(input.getCatalogEntryId(), name.getId(), name.getLanguageTag(), name.getValue());
    //         return payloadMapper.toAddDescriptionPayload(item);
    //     };
    // }

    @MutationMapping
    protected AddDescriptionPayload addDescription(@Argument AddDescriptionInput input) {
        final TranslationInput name = input.getDescription();
        final XtdConcept item = conceptRecordService.addDescription(input.getCatalogEntryId(), name.getId(), name.getLanguageTag(), name.getValue());
        return payloadMapper.toAddDescriptionPayload(item);
    }

    // protected DataFetcher<UpdateDescriptionPayload> updateDescription() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final UpdateDescriptionInput input = OBJECT_MAPPER.convertValue(argument, UpdateDescriptionInput.class);
    //         final XtdText item = textRecordService.updateText(input.getDescriptionId(), input.getValue());
    //         return payloadMapper.toUpdateDescriptionPayload(item);
    //     };
    // }

    @MutationMapping
    protected UpdateDescriptionPayload updateDescription(@Argument UpdateDescriptionInput input) {
        final XtdText item = textRecordService.updateText(input.getDescriptionId(), input.getValue());
        return payloadMapper.toUpdateDescriptionPayload(item);
    }

    // protected DataFetcher<DeleteDescriptionPayload> deleteDescription() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final DeleteDescriptionInput input = OBJECT_MAPPER.convertValue(argument, DeleteDescriptionInput.class);
    //         final XtdText item = textRecordService.deleteText(input.getDescriptionId());
    //         return payloadMapper.toDeleteDescriptionPayload(item);
    //     };
    // }

    @MutationMapping
    protected DeleteDescriptionPayload deleteDescription(@Argument DeleteDescriptionInput input) {
        final XtdText item = textRecordService.deleteText(input.getDescriptionId());
        return payloadMapper.toDeleteDescriptionPayload(item);
    }

    @MutationMapping
    protected AddCommentPayload addComment(@Argument AddCommentInput input) {
        final XtdObject item = objectRecordService.addComment(input);
        return payloadMapper.toAddCommentPayload(item);
    }

    // protected DataFetcher<UpdateCommentPayload> updateComment() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final UpdateCommentInput input = OBJECT_MAPPER.convertValue(argument, UpdateCommentInput.class);
    //         final XtdText item = textRecordService.updateText(input.getCommentId(), input.getValue());
    //         return payloadMapper.toUpdateCommentPayload(item);
    //     };
    // }

    @MutationMapping
    protected UpdateCommentPayload updateComment(@Argument UpdateCommentInput input) {
        final XtdText item = textRecordService.updateText(input.getCommentId(), input.getValue());
        return payloadMapper.toUpdateCommentPayload(item);
    }

    // protected DataFetcher<DeleteCommentPayload> deleteComment() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final DeleteCommentInput input = OBJECT_MAPPER.convertValue(argument, DeleteCommentInput.class);
    //         final XtdText item = textRecordService.deleteText(input.getCommentId());
    //         return payloadMapper.toDeleteCommentPayload(item);
    //     };
    // }

    @MutationMapping
    protected DeleteCommentPayload deleteComment(@Argument DeleteCommentInput input) {
        final XtdText item = textRecordService.deleteText(input.getCommentId());
        return payloadMapper.toDeleteCommentPayload(item);
    }

    // private DataFetcher<CreateTagPayload> createTag() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final CreateTagInput input = OBJECT_MAPPER.convertValue(argument, CreateTagInput.class);
    //         final Tag tag = catalogService.createTag(input.getTagId(), input.getName());
    //         return new CreateTagPayload(tag);
    //     };
    // }

    @MutationMapping
    protected CreateTagPayload createTag(@Argument CreateTagInput input) {
        final Tag tag = catalogService.createTag(input.getTagId(), input.getName());
        return new CreateTagPayload(tag);
    }

    // private DataFetcher<UpdateTagPayload> updateTag() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final UpdateTagInput input = OBJECT_MAPPER.convertValue(argument, UpdateTagInput.class);
    //         final Tag tag = catalogService.updateTag(input.getTagId(), input.getName());
    //         return new UpdateTagPayload(tag);
    //     };
    // }

    @MutationMapping
    protected UpdateTagPayload updateTag(@Argument UpdateTagInput input) {
        final Tag tag = catalogService.updateTag(input.getTagId(), input.getName());
        return new UpdateTagPayload(tag);
    }

    // private DataFetcher<DeleteTagPayload> deleteTag() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final DeleteTagInput input = OBJECT_MAPPER.convertValue(argument, DeleteTagInput.class);
    //         final Tag tag = catalogService.deleteTag(input.getTagId());
    //         return new DeleteTagPayload(tag);
    //     };
    // }

    @MutationMapping
    protected DeleteTagPayload deleteTag(@Argument DeleteTagInput input) {
        final Tag tag = catalogService.deleteTag(input.getTagId());
        return new DeleteTagPayload(tag);
    }

    // private DataFetcher<AddTagPayload> addTag() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final AddTagInput input = OBJECT_MAPPER.convertValue(argument, AddTagInput.class);
    //         final CatalogRecord catalogRecord = catalogService.addTag(input.getCatalogEntryId(), input.getTagId());
    //         final Tag tag = tagService.findById(input.getTagId()).orElseThrow();
    //         return new AddTagPayload(catalogRecord, tag);
    //     };
    // }

    @MutationMapping
    protected AddTagPayload addTag(@Argument AddTagInput input) {
        final CatalogRecord catalogRecord = catalogService.addTag(input.getCatalogEntryId(), input.getTagId());
        final Tag tag = tagService.findById(input.getTagId()).orElseThrow();
        return new AddTagPayload(catalogRecord, tag);
    }

    // private DataFetcher<RemoveTagPayload> removeTag() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final RemoveTagInput input = OBJECT_MAPPER.convertValue(argument, RemoveTagInput.class);
    //         final CatalogRecord catalogRecord = catalogService.removeTag(input.getCatalogEntryId(), input.getTagId());
    //         final Tag tag = tagService.findById(input.getTagId()).orElseThrow();
    //         return new RemoveTagPayload(catalogRecord, tag);
    //     };
    // }

    @MutationMapping
    protected RemoveTagPayload removeTag(@Argument RemoveTagInput input) {
        final CatalogRecord catalogRecord = catalogService.removeTag(input.getCatalogEntryId(), input.getTagId());
        final Tag tag = tagService.findById(input.getTagId()).orElseThrow();
        return new RemoveTagPayload(catalogRecord, tag);
    }

    @MutationMapping
    protected UpdateStatusPayload updateStatus(@Argument UpdateStatusInput input) {
        final XtdObject item = objectRecordService.updateStatus(input.getCatalogEntryId(), input.getStatus());
        return payloadMapper.toUpdateStatusPayload(item);
    }

    @MutationMapping
    protected UpdateMajorVersionPayload updateMajorVersion(@Argument UpdateMajorVersionInput input) {
        final XtdObject item = objectRecordService.updateMajorVersion(input.getCatalogEntryId(), input.getMajorVersion());
        return payloadMapper.toUpdateMajorVersionPayload(item);
    }

    @MutationMapping
    protected UpdateMinorVersionPayload updateMinorVersion(@Argument UpdateMinorVersionInput input) {
        final XtdObject item = objectRecordService.updateMinorVersion(input.getCatalogEntryId(), input.getMinorVersion());
        return payloadMapper.toUpdateMinorVersionPayload(item);
    }
}
