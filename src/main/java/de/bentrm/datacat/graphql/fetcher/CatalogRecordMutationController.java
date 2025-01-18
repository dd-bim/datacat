package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.catalog.service.SimpleRecordServiceFactory;
import de.bentrm.datacat.graphql.input.CatalogEntryPropertiesInput;
import de.bentrm.datacat.graphql.input.CreateEntryInput;
import de.bentrm.datacat.graphql.payload.CreateEntryPayload;
import de.bentrm.datacat.graphql.payload.PayloadMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class CatalogRecordMutationController {

    private final PayloadMapper PAYLOAD_MAPPER = PayloadMapper.INSTANCE;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private SimpleRecordServiceFactory simpleRecordServiceFactory;


    @MutationMapping
    public CreateEntryPayload createCatalogEntry(@Argument CreateEntryInput input) {
        CatalogEntryPropertiesInput properties = input.getProperties();

        final SimpleRecordService<?> catalogRecordService = simpleRecordServiceFactory.getService(input.getCatalogEntryType());
        final CatalogRecord newRecord = catalogRecordService.addRecord(properties);

        if (input.getTags() != null) {
            input.getTags().forEach(tagId -> catalogService.addTag(newRecord.getId(), tagId));
        }

        return PAYLOAD_MAPPER.toCreateEntryPayload(newRecord);
    }

    // protected DataFetcher<DeleteCatalogEntryPayload> deleteCatalogEntry() {
    //     return environment -> {
    //         final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
    //         final DeleteCatalogEntryInput input = OBJECT_MAPPER.convertValue(argument, DeleteCatalogEntryInput.class);

    //         final String recordId = input.getCatalogEntryId();
    //         final SimpleRecordService<?> simpleCatalogRecordService = this.getCatalogRecordService(recordId);

    //         final CatalogRecord entry = catalogService.getEntryById(recordId).orElseThrow();
    //         if (entry instanceof XtdObject xtdObject) {

    //                 Set<XtdMultiLanguageText> multiLanguageTexts = xtdObject.getNames();
    //                 multiLanguageTexts.addAll(xtdObject.getComments());
    //                 if (entry instanceof XtdConcept xtdConcept) {
    //                     multiLanguageTexts.addAll(xtdConcept.getDescriptions());
    //                 }

    //                 for (XtdMultiLanguageText multiLanguageText : multiLanguageTexts) {
    //                     XtdMultiLanguageText multiLanguage = (XtdMultiLanguageText) catalogService.getEntryById(multiLanguageText.getId()).orElseThrow();
    //                     Set<XtdText> texts = multiLanguage.getTexts();
    //                     for (XtdText text : texts) {
    //                         // Delete XtdText
    //                         final SimpleRecordService<?> textService = this.getCatalogRecordService(text.getId());
    //                         textService.removeRecord(text.getId());
    //                     }
    //                      // Delete XtdMultiLanguageText
    //                     final SimpleRecordService<?> languageTextService = this.getCatalogRecordService(multiLanguage.getId());
    //                     languageTextService.removeRecord(multiLanguage.getId());
    //                 }
    //             }

    //         final CatalogRecord deletedRecord = simpleCatalogRecordService.removeRecord(recordId);

    //         return PAYLOAD_MAPPER.toDeleteEntryPayload(deletedRecord);
    //     };
    // }


}
