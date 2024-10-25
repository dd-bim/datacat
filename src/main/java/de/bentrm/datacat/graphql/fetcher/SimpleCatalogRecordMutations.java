package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdSubdivision;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.repository.CountryRepository;
import de.bentrm.datacat.catalog.repository.ExternalDocumentRepository;
import de.bentrm.datacat.catalog.repository.IntervalRepository;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.repository.OrderedValueRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.repository.SubdivisionRepository;
import de.bentrm.datacat.catalog.repository.TextRepository;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.graphql.input.ApiInputMapper;
import de.bentrm.datacat.graphql.input.CatalogEntryPropertiesInput;
import de.bentrm.datacat.graphql.input.CreateEntryInput;
import de.bentrm.datacat.graphql.input.DeleteCatalogEntryInput;
import de.bentrm.datacat.graphql.input.ExternalDocumentInput;
import de.bentrm.datacat.graphql.input.PropertyInput;
import de.bentrm.datacat.graphql.input.TranslationInput;
import de.bentrm.datacat.graphql.input.UnitInput;
import de.bentrm.datacat.graphql.payload.CreateEntryPayload;
import de.bentrm.datacat.graphql.payload.DeleteCatalogEntryPayload;
import de.bentrm.datacat.graphql.payload.PayloadMapper;
import graphql.schema.DataFetcher;
import kotlin.Unit;
import lombok.extern.slf4j.Slf4j;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.management.relation.Relation;

@Slf4j
@Component
public class SimpleCatalogRecordMutations implements MutationFetchers {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final PayloadMapper PAYLOAD_MAPPER = PayloadMapper.INSTANCE;

    private final CatalogService catalogService;
    private final ObjectRecordService objectRecordService;
    private final ConceptRecordService conceptRecordService;
    private final ObjectRepository objectRepository;
    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;
    private final ExternalDocumentRepository externalDocumentRepository;
    private final CountryRepository countryRepository;
    private final SubdivisionRepository subdivisionRepository;
    private final ValueRepository valueRepository;
    private final OrderedValueRepository orderedValueRepository;
    private final IntervalRepository intervalRepository;
    private final LanguageRepository languageRepository;
    private final TextRepository textRepository;
    private final Map<CatalogRecordType, SimpleRecordService<?>> mappedCatalogRecordServices;

    public SimpleCatalogRecordMutations(CatalogService catalogService,
            ObjectRecordService objectRecordService,
            ConceptRecordService conceptRecordService,
            ObjectRepository objectRepository,
            PropertyRepository propertyRepository,
            UnitRepository unitRepository,
            ExternalDocumentRepository externalDocumentRepository,
            CountryRepository countryRepository,
            SubdivisionRepository subdivisionRepository,
            ValueRepository valueRepository,
            OrderedValueRepository orderedValueRepository,
            IntervalRepository intervalRepository,
            LanguageRepository languageRepository,
            TextRepository textRepository,
            List<SimpleRecordService<?>> simpleCatalogRecordServices) {
        this.catalogService = catalogService;
        this.objectRecordService = objectRecordService;
        this.conceptRecordService = conceptRecordService;
        this.objectRepository = objectRepository;
        this.propertyRepository = propertyRepository;
        this.unitRepository = unitRepository;
        this.externalDocumentRepository = externalDocumentRepository;
        this.countryRepository = countryRepository;
        this.subdivisionRepository = subdivisionRepository;
        this.valueRepository = valueRepository;
        this.orderedValueRepository = orderedValueRepository;
        this.intervalRepository = intervalRepository;
        this.languageRepository = languageRepository;
        this.textRepository = textRepository;

        this.mappedCatalogRecordServices = simpleCatalogRecordServices.stream()
                .collect(Collectors.toMap(
                        SimpleRecordService::getSupportedCatalogRecordType,
                        Function.identity()));
    }

    @Override
    public Map<String, DataFetcher> getMutationFetchers() {
        return Map.of(
                "createCatalogEntry", createCatalogEntry(),
                "deleteCatalogEntry", deleteCatalogEntry());
    }

    protected DataFetcher<CreateEntryPayload> createCatalogEntry() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final CreateEntryInput input = OBJECT_MAPPER.convertValue(argument, CreateEntryInput.class);
            final CatalogEntryPropertiesInput properties = input.getProperties();

            final SimpleRecordService<?> catalogRecordService = this
                    .getCatalogRecordService(input.getCatalogEntryType());
            final CatalogRecord newRecord = catalogRecordService.addRecord(properties);

            if (input.getTags() != null) {
                input.getTags().forEach(tagId -> catalogService.addTag(newRecord.getId(), tagId));
            }

            return PAYLOAD_MAPPER.toCreateEntryPayload(newRecord);
        };
    }

    protected DataFetcher<DeleteCatalogEntryPayload> deleteCatalogEntry() {
        return environment -> {
            final Map<String, Object> argument = environment.getArgument(INPUT_ARGUMENT);
            final DeleteCatalogEntryInput input = OBJECT_MAPPER.convertValue(argument, DeleteCatalogEntryInput.class);

            final String recordId = input.getCatalogEntryId();
            final SimpleRecordService<?> simpleCatalogRecordService = this.getCatalogRecordService(recordId);

            final CatalogRecord entry = catalogService.getEntryById(recordId).orElseThrow();
            if (entry instanceof XtdObject xtdObject) {

                    Set<XtdMultiLanguageText> multiLanguageTexts = xtdObject.getNames();
                    multiLanguageTexts.addAll(xtdObject.getComments());
                    if (entry instanceof XtdConcept xtdConcept) {
                        multiLanguageTexts.addAll(xtdConcept.getDescriptions());
                    }

                    for (XtdMultiLanguageText multiLanguageText : multiLanguageTexts) {
                        XtdMultiLanguageText multiLanguage = (XtdMultiLanguageText) catalogService.getEntryById(multiLanguageText.getId()).orElseThrow();
                        Set<XtdText> texts = multiLanguage.getTexts();
                        for (XtdText text : texts) {
                            // Delete XtdText
                            final SimpleRecordService<?> textService = this.getCatalogRecordService(text.getId());
                            textService.removeRecord(text.getId());
                        }
                         // Delete XtdMultiLanguageText
                        final SimpleRecordService<?> languageTextService = this.getCatalogRecordService(multiLanguage.getId());
                        languageTextService.removeRecord(multiLanguage.getId());
                    }
                }

            final CatalogRecord deletedRecord = simpleCatalogRecordService.removeRecord(recordId);

            return PAYLOAD_MAPPER.toDeleteEntryPayload(deletedRecord);
        };
    }

    @NotNull
    private SimpleRecordService<?> getCatalogRecordService(String recordId) {
        // fetch relationship using base repository
        final CatalogRecord catalogRecord = catalogService
                .getEntryById(recordId)
                .orElseThrow();

        // execute action using concrete service implementation
        final String className = catalogRecord.getClass().getSimpleName();
        final String typeName = className.replace("Xtd", "");
        final CatalogRecordType recordType = CatalogRecordType.valueOf(typeName);
        return getCatalogRecordService(recordType);
    }

    @NotNull
    private SimpleRecordService<?> getCatalogRecordService(CatalogRecordType relationshipType) {
        final SimpleRecordService<?> simpleCatalogRecordService = mappedCatalogRecordServices
                .get(relationshipType);
        if (simpleCatalogRecordService == null)
            throw new IllegalArgumentException("Unsupported catalog record type");
        return simpleCatalogRecordService;
    }
}
