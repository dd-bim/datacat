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
    protected AddTextPayload addName(@Argument AddTextInput input) {
        final XtdObject item = objectRecordService.addName(input);
        return payloadMapper.toAddTextPayload(item);
    }

    @MutationMapping
    protected UpdateTextPayload updateName(@Argument UpdateTextInput input) {
        final XtdText item = textRecordService.updateText(input.getTextId(), input.getValue());
        return payloadMapper.toUpdateTextPayload(item);
    }

    @MutationMapping
    protected DeleteTextPayload deleteName(@Argument DeleteTextInput input) {
        TextCountResult textCount = textRecordService.countTexts(input.getTextId());
        final XtdText item = textRecordService.deleteText(input.getTextId());
        if (textCount.getTextNumber() == 1) {
            catalogCleanupService.deleteNodeWithRelationships(textCount.getId());
        }
        return payloadMapper.toDeleteTextPayload(item);
    }

    @MutationMapping
    protected AddTextPayload addDescription(@Argument AddTextInput input) {
        final XtdConcept item = conceptRecordService.addDescription(input);
        return payloadMapper.toAddTextPayload(item);
    }

    @MutationMapping
    protected UpdateTextPayload updateDescription(@Argument UpdateTextInput input) {
        final XtdText item = textRecordService.updateText(input.getTextId(), input.getValue());
        return payloadMapper.toUpdateTextPayload(item);
    }

    @MutationMapping
    protected DeleteTextPayload deleteDescription(@Argument DeleteTextInput input) {
        TextCountResult textCount = textRecordService.countTexts(input.getTextId());
        final XtdText item = textRecordService.deleteText(input.getTextId());
        if (textCount.getTextNumber() == 1) {
            catalogCleanupService.deleteNodeWithRelationships(textCount.getId());
        }
        return payloadMapper.toDeleteTextPayload(item);
    }

    @MutationMapping
    protected AddTextPayload addComment(@Argument AddTextInput input) {
        final XtdObject item = objectRecordService.addComment(input);
        return payloadMapper.toAddTextPayload(item);
    }

    @MutationMapping
    protected UpdateTextPayload updateComment(@Argument UpdateTextInput input) {
        final XtdText item = textRecordService.updateText(input.getTextId(), input.getValue());
        return payloadMapper.toUpdateTextPayload(item);
    }

    @MutationMapping
    protected DeleteTextPayload deleteComment(@Argument DeleteTextInput input) {
        TextCountResult textCount = textRecordService.countTexts(input.getTextId());
        final XtdText item = textRecordService.deleteText(input.getTextId());
        if (textCount.getTextNumber() == 1) {
            catalogCleanupService.deleteNodeWithRelationships(textCount.getId());
        }
        return payloadMapper.toDeleteTextPayload(item);
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

    @MutationMapping
    protected AddTextPayload addDeprecationExplanation(@Argument AddTextInput input) {
        final XtdObject item = objectRecordService.addDeprecationExplanation(input);
        return payloadMapper.toAddTextPayload(item);
    }

    @MutationMapping
    protected UpdateTextPayload updateDeprecationExplanation(@Argument UpdateTextInput input) {
        final XtdText item = textRecordService.updateText(input.getTextId(), input.getValue());
        return payloadMapper.toUpdateTextPayload(item);
    }

    @MutationMapping
    protected DeleteTextPayload deleteDeprecationExplanation(@Argument DeleteTextInput input) {
        TextCountResult textCount = textRecordService.countTexts(input.getTextId());
        final XtdText item = textRecordService.deleteText(input.getTextId());
        if (textCount.getTextNumber() == 1) {
            catalogCleanupService.deleteNodeWithRelationships(textCount.getId());
        }
        return payloadMapper.toDeleteTextPayload(item);
    }

    @MutationMapping
    protected AddTextPayload addExample(@Argument AddTextInput input) {
        final XtdObject item = conceptRecordService.addExample(input);
        return payloadMapper.toAddTextPayload(item);
    }

    @MutationMapping
    protected UpdateTextPayload updateExample(@Argument UpdateTextInput input) {
        final XtdText item = textRecordService.updateText(input.getTextId(), input.getValue());
        return payloadMapper.toUpdateTextPayload(item);
    }

    @MutationMapping
    protected DeleteTextPayload deleteExample(@Argument DeleteTextInput input) {
        TextCountResult textCount = textRecordService.countTexts(input.getTextId());
        final XtdText item = textRecordService.deleteText(input.getTextId());
        if (textCount.getTextNumber() == 1) {
            catalogCleanupService.deleteNodeWithRelationships(textCount.getId());
        }
        return payloadMapper.toDeleteTextPayload(item);
    }

    @MutationMapping
    protected AddTextPayload addDefinition(@Argument AddTextInput input) {
        final XtdObject item = conceptRecordService.addDefinition(input);
        return payloadMapper.toAddTextPayload(item);
    }

    @MutationMapping
    protected UpdateTextPayload updateDefinition(@Argument UpdateTextInput input) {
        final XtdText item = textRecordService.updateText(input.getTextId(), input.getValue());
        return payloadMapper.toUpdateTextPayload(item);
    }

    @MutationMapping
    protected DeleteTextPayload deleteDefinition(@Argument DeleteTextInput input) {
        TextCountResult textCount = textRecordService.countTexts(input.getTextId());
        final XtdText item = textRecordService.deleteText(input.getTextId());
        if (textCount.getTextNumber() == 1) {
            catalogCleanupService.deleteNodeWithRelationships(textCount.getId());
        }
        return payloadMapper.toDeleteTextPayload(item);
    }

    @MutationMapping
    protected AddTextPayload addCountryOfOrigin(@Argument AddCountryInput input) {
        final XtdConcept item = conceptRecordService.addCountryOfOrigin(input);
        return payloadMapper.toAddTextPayload(item);
    }

    @MutationMapping
    protected DeleteRelationshipPayload deleteCountryOfOrigin(@Argument DeleteCountryOfOriginInput input) {
        final XtdConcept item = conceptRecordService.deleteCountryOfOrigin(input);
        return payloadMapper.toDeleteRelationshipPayload(item);
    }
}
