package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.domain.XtdUnit;
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

import java.util.HashSet;
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
                multiLanguageTexts.addAll(xtdConcept.getExamples());
                multiLanguageTexts.add(xtdConcept.getDefinition());
            }
            if (entry instanceof XtdUnit xtdUnit) {
                multiLanguageTexts.add(xtdUnit.getSymbol());
            }
            multiLanguageTexts.remove(null);

            for (XtdMultiLanguageText multiLanguageText : multiLanguageTexts) {
                removeMultiLanguageText(multiLanguageText.getId());
            }
        } else if (entry instanceof XtdSymbol xtdSymbol) {
            XtdText text = xtdSymbol.getSymbol();
            // Delete XtdText
            removeText(text.getId());
        } else if (entry instanceof XtdDictionary xtdDictionary) {
            XtdMultiLanguageText multiLanguageText = xtdDictionary.getName();
            removeMultiLanguageText(multiLanguageText.getId());
        }

        if (entry instanceof XtdUnit xtdUnit) {
            XtdRational offset = xtdUnit.getOffset();
            XtdRational coefficient = xtdUnit.getCoefficient();
            // Delete XtdRational
            if (offset != null) {
                removeRational(offset.getId());
            }
            if (coefficient != null) {
                removeRational(coefficient.getId());
            }

        }

        if (entry instanceof XtdDimension dimension) {
            Set<XtdRational> rationals = new HashSet<XtdRational>();
            rationals.add(dimension.getAmountOfSubstanceExponent());
            rationals.add(dimension.getElectricCurrentExponent());
            rationals.add(dimension.getLuminousIntensityExponent());
            rationals.add(dimension.getLengthExponent());
            rationals.add(dimension.getMassExponent());
            rationals.add(dimension.getThermodynamicTemperatureExponent());
            rationals.add(dimension.getTimeExponent());
            rationals.remove(null);

            for (XtdRational rational : rationals) {
                removeRational(rational.getId());
            }
        }

        final CatalogRecord deletedRecord = catalogRecordService.removeRecord(recordId);

        return PAYLOAD_MAPPER.toDeleteEntryPayload(deletedRecord);
    }

    public void removeText(String textId) {
        final SimpleRecordService<?> textService = simpleRecordServiceFactory.getService(CatalogRecordType.Text);
        textService.removeRecord(textId);
    }

    public void removeMultiLanguageText(String multiLanguageTextId) {
        XtdMultiLanguageText multiLanguage = (XtdMultiLanguageText) catalogService.getEntryById(multiLanguageTextId)
                .orElseThrow(
                        () -> new IllegalArgumentException("No record with id " + multiLanguageTextId + " found."));
        Set<XtdText> texts = multiLanguage.getTexts();
        for (XtdText text : texts) {
            removeText(text.getId());
        }
        final SimpleRecordService<?> languageTextService = simpleRecordServiceFactory
                .getService(CatalogRecordType.MultiLanguageText);
        languageTextService.removeRecord(multiLanguageTextId);
    }

    public void removeRational(String rationalId) {
        final SimpleRecordService<?> rationalService = simpleRecordServiceFactory
                .getService(CatalogRecordType.Rational);
        rationalService.removeRecord(rationalId);
    }

}
