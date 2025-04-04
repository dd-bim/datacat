package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.catalog.service.SimpleRecordServiceFactory;
import de.bentrm.datacat.graphql.input.CatalogEntryPropertiesInput;
import de.bentrm.datacat.graphql.input.CreateEntryInput;
import de.bentrm.datacat.graphql.input.DeleteCatalogEntryInput;
import de.bentrm.datacat.graphql.payload.CreateEntryPayload;
import de.bentrm.datacat.graphql.payload.DeleteCatalogEntryPayload;
import de.bentrm.datacat.graphql.payload.PayloadMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

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

        final SimpleRecordService<?> catalogRecordService = simpleRecordServiceFactory
                .getService(input.getCatalogEntryType());
        final CatalogRecord newRecord = catalogRecordService.addRecord(properties);

        if (input.getTags() != null) {
            input.getTags().forEach(tagId -> catalogService.addTag(newRecord.getId(), tagId));
        }

        return PAYLOAD_MAPPER.toCreateEntryPayload(newRecord);
    }

    @MutationMapping
    protected DeleteCatalogEntryPayload deleteCatalogEntry(@Argument DeleteCatalogEntryInput input) {

        final String recordId = input.getCatalogEntryId();
        final CatalogRecord entry = catalogService.getEntryById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Catalog entry not found"));
        final SimpleRecordService<?> catalogRecordService = simpleRecordServiceFactory
                .getService(CatalogRecordType.getByDomainClass(entry));

        if (entry instanceof XtdObject xtdObject) {

            Set<XtdMultiLanguageText> multiLanguageTexts = xtdObject.getNames();
            multiLanguageTexts.addAll(xtdObject.getComments());
            multiLanguageTexts.add(xtdObject.getDeprecationExplanation());
            if (entry instanceof XtdConcept xtdConcept) {
                multiLanguageTexts.addAll(xtdConcept.getDescriptions());
            }

            for (XtdMultiLanguageText multiLanguageText : multiLanguageTexts) {
                XtdMultiLanguageText multiLanguage = (XtdMultiLanguageText) catalogService
                        .getEntryById(multiLanguageText.getId()).orElseThrow(() -> new IllegalArgumentException(
                                "No record with id " + multiLanguageText.getId() + " found."));
                Set<XtdText> texts = multiLanguage.getTexts();
                for (XtdText text : texts) {
                    // Delete XtdText
                    final XtdText textEntity = (XtdText) catalogService.getEntryById(text.getId()).orElseThrow(
                            () -> new IllegalArgumentException("No record with id " + text.getId() + " found."));
                    final SimpleRecordService<?> textService = simpleRecordServiceFactory
                            .getService(CatalogRecordType.getByDomainClass(textEntity));
                    textService.removeRecord(text.getId());
                }
                // Delete XtdMultiLanguageText
                final SimpleRecordService<?> languageTextService = simpleRecordServiceFactory
                        .getService(CatalogRecordType.getByDomainClass(multiLanguage));
                languageTextService.removeRecord(multiLanguage.getId());
            }
        } else if (entry instanceof XtdSymbol xtdSymbol) {
            XtdText text = xtdSymbol.getSymbol();
            // Delete XtdText
            final XtdText textEntity = (XtdText) catalogService.getEntryById(text.getId()).orElseThrow(
                () -> new IllegalArgumentException("No record with id " + text.getId() + " found."));
            final SimpleRecordService<?> textService = simpleRecordServiceFactory
                .getService(CatalogRecordType.getByDomainClass(textEntity));
            textService.removeRecord(text.getId());
        } else if (entry instanceof XtdDictionary xtdDictionary) {
            XtdMultiLanguageText multiLanguageText = xtdDictionary.getName();
            // Delete XtdMultiLanguageText
            final XtdMultiLanguageText multiLanguage = (XtdMultiLanguageText) catalogService
                    .getEntryById(multiLanguageText.getId()).orElseThrow(() -> new IllegalArgumentException(
                            "No record with id " + multiLanguageText.getId() + " found."));
            Set<XtdText> texts = multiLanguage.getTexts();
            for (XtdText text : texts) {
                // Delete XtdText
                final XtdText textEntity = (XtdText) catalogService.getEntryById(text.getId()).orElseThrow(
                        () -> new IllegalArgumentException("No record with id " + text.getId() + " found."));
                final SimpleRecordService<?> textService = simpleRecordServiceFactory
                        .getService(CatalogRecordType.getByDomainClass(textEntity));
                textService.removeRecord(text.getId());
            }
            // Delete XtdMultiLanguageText
            final SimpleRecordService<?> languageTextService = simpleRecordServiceFactory
                    .getService(CatalogRecordType.getByDomainClass(multiLanguage));
            languageTextService.removeRecord(multiLanguage.getId());
        }

        final CatalogRecord deletedRecord = catalogRecordService.removeRecord(recordId);

        return PAYLOAD_MAPPER.toDeleteEntryPayload(deletedRecord);
    }

}
