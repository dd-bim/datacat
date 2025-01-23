package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.TagService;
import de.bentrm.datacat.catalog.service.TextRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.graphql.dto.TextCountResult;
import de.bentrm.datacat.graphql.input.*;
import de.bentrm.datacat.graphql.payload.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Slf4j
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

    @Autowired
    private CatalogCleanupService catalogCleanupService;

    @MutationMapping
    protected AddNamePayload addName(@Argument AddNameInput input) {
        final XtdObject item = objectRecordService.addName(input);
        return payloadMapper.toAddNamePayload(item);
    }

    @MutationMapping
    protected UpdateNamePayload updateName(@Argument UpdateNameInput input) {
        final XtdText item = textRecordService.updateText(input.getNameId(), input.getValue());
        return payloadMapper.toUpdateNamePayload(item);
    }

    @MutationMapping
    protected DeleteNamePayload deleteName(@Argument DeleteNameInput input) {
        TextCountResult textCount = textRecordService.countTexts(input.getNameId());
        final XtdText item = textRecordService.deleteText(input.getNameId());
        if (textCount.getTextNumber() == 1) {
            catalogCleanupService.deleteNodeWithRelationships(textCount.getId());
        }
        return payloadMapper.toDeleteNamePayload(item);
    }

    @MutationMapping
    protected AddDescriptionPayload addDescription(@Argument AddDescriptionInput input) {
        final XtdConcept item = conceptRecordService.addDescription(input);
        return payloadMapper.toAddDescriptionPayload(item);
    }

    @MutationMapping
    protected UpdateDescriptionPayload updateDescription(@Argument UpdateDescriptionInput input) {
        final XtdText item = textRecordService.updateText(input.getDescriptionId(), input.getValue());
        return payloadMapper.toUpdateDescriptionPayload(item);
    }

    @MutationMapping
    protected DeleteDescriptionPayload deleteDescription(@Argument DeleteDescriptionInput input) {
        TextCountResult textCount = textRecordService.countTexts(input.getDescriptionId());
        final XtdText item = textRecordService.deleteText(input.getDescriptionId());
        if (textCount.getTextNumber() == 1) {
            catalogCleanupService.deleteNodeWithRelationships(textCount.getId());
        }
        return payloadMapper.toDeleteDescriptionPayload(item);
    }

    @MutationMapping
    protected AddCommentPayload addComment(@Argument AddCommentInput input) {
        final XtdObject item = objectRecordService.addComment(input);
        return payloadMapper.toAddCommentPayload(item);
    }

    @MutationMapping
    protected UpdateCommentPayload updateComment(@Argument UpdateCommentInput input) {
        final XtdText item = textRecordService.updateText(input.getCommentId(), input.getValue());
        return payloadMapper.toUpdateCommentPayload(item);
    }

    @MutationMapping
    protected DeleteCommentPayload deleteComment(@Argument DeleteCommentInput input) {
        TextCountResult textCount = textRecordService.countTexts(input.getCommentId());
        final XtdText item = textRecordService.deleteText(input.getCommentId());
        if (textCount.getTextNumber() == 1) {
            catalogCleanupService.deleteNodeWithRelationships(textCount.getId());
        }
        return payloadMapper.toDeleteCommentPayload(item);
    }

    @MutationMapping
    protected CreateTagPayload createTag(@Argument CreateTagInput input) {
        final Tag tag = catalogService.createTag(input.getTagId(), input.getName());
        return new CreateTagPayload(tag);
    }

    @MutationMapping
    protected UpdateTagPayload updateTag(@Argument UpdateTagInput input) {
        final Tag tag = catalogService.updateTag(input.getTagId(), input.getName());
        return new UpdateTagPayload(tag);
    }

    @MutationMapping
    protected DeleteTagPayload deleteTag(@Argument DeleteTagInput input) {
        final Tag tag = catalogService.deleteTag(input.getTagId());
        return new DeleteTagPayload(tag);
    }

    @MutationMapping
    protected AddTagPayload addTag(@Argument AddTagInput input) {
        final CatalogRecord catalogRecord = catalogService.addTag(input.getCatalogEntryId(), input.getTagId());
        final Tag tag = tagService.findById(input.getTagId()).orElseThrow(() -> new IllegalArgumentException("No tag with id " + input.getTagId() + " found."));
        return new AddTagPayload(catalogRecord, tag);
    }

    @MutationMapping
    protected RemoveTagPayload removeTag(@Argument RemoveTagInput input) {
        final CatalogRecord catalogRecord = catalogService.removeTag(input.getCatalogEntryId(), input.getTagId());
        final Tag tag = tagService.findById(input.getTagId()).orElseThrow(() -> new IllegalArgumentException("No tag with id " + input.getTagId() + " found."));
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
