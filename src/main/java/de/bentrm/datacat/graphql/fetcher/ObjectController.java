package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.input.LocalizationInput;
import de.bentrm.datacat.util.LocalizationUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ObjectController {

    @Autowired
    private ObjectRecordService service;
    
    @Autowired
    private CatalogService catalogService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdObject> getObject(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdObject.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdObject> findObjects(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdObject> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdObject", field = "names")
    public Map<XtdObject, List<XtdMultiLanguageText>> getNames(List<XtdObject> objects) {
        return objects.stream()
                .filter(object -> object != null)  // Filter out null objects
                .collect(Collectors.toMap(
                        object -> object,
                        object -> {
                            List<XtdMultiLanguageText> result = service.getNames(object);
                            return result != null ? result : List.of();  // Handle null List
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdObject", field = "dictionary")
    public Map<XtdObject, Optional<XtdDictionary>> getDictionary(List<XtdObject> objects) {
        return objects.stream()
                .filter(object -> object != null)  // Filter out null objects
                .collect(Collectors.toMap(
                        object -> object,
                        object -> {
                            Optional<XtdDictionary> result = service.getDictionary(object);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdObject", field = "deprecationExplanation")
    public Map<XtdObject, Optional<XtdMultiLanguageText>> getDeprecationExplanation(List<XtdObject> objects) {
        return objects.stream()
                .filter(object -> object != null)  // Filter out null objects
                .collect(Collectors.toMap(
                        object -> object,
                        object -> {
                            Optional<XtdMultiLanguageText> result = service.getDeprecationExplanation(object);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdObject", field = "replacedObjects")
    public Map<XtdObject, List<XtdObject>> getReplacedObjects(List<XtdObject> objects) {
        return objects.stream()
                .filter(object -> object != null)  // Filter out null objects
                .collect(Collectors.toMap(
                        object -> object,
                        object -> {
                            List<XtdObject> result = service.getReplacedObjects(object);
                            return result != null ? result : List.of();  // Handle null List
                        }
                ));
    }

    @BatchMapping(typeName = "XtdObject", field = "replacingObjects")
    public Map<XtdObject, List<XtdObject>> getReplacingObjects(List<XtdObject> objects) {
        return objects.stream()
                .filter(object -> object != null)  // Filter out null objects
                .collect(Collectors.toMap(
                        object -> object,
                        object -> {
                            List<XtdObject> result = service.getReplacingObjects(object);
                            return result != null ? result : List.of();  // Handle null List
                        }
                ));
    }

    @BatchMapping(typeName = "XtdObject", field = "comments")
    public Map<XtdObject, List<XtdMultiLanguageText>> getComments(List<XtdObject> objects) {
        return objects.stream()
                .filter(object -> object != null)  // Filter out null objects
                .collect(Collectors.toMap(
                        object -> object,
                        object -> {
                            List<XtdMultiLanguageText> result = service.getComments(object);
                            return result != null ? result : List.of();  // Handle null List
                        }
                ));                
    }
    
    @BatchMapping(typeName = "XtdObject", field = "name")
    public Map<XtdObject, Optional<String>> getName(
            List<XtdObject> objects, 
            org.dataloader.BatchLoaderEnvironment environment) {
        // long startTime = System.currentTimeMillis();
        
        // Filter out null objects first
        List<XtdObject> validObjects = objects.stream()
                .filter(object -> object != null)
                .toList();
        
        // log.info("=== BatchMapping name START: Processing {} objects ({} valid) ===", 
                //  objects.size(), validObjects.size());
        
        if (validObjects.isEmpty()) {
            log.warn("=== BatchMapping name: No valid objects to process ===");
            return Map.of();
        }
        
        // Extract IDs
        List<String> objectIds = validObjects.stream()
                .map(XtdObject::getId)
                .toList();
        
        // Get the input argument from the environment
        LocalizationInput input = environment.getKeyContextsList().isEmpty() 
            ? null 
            : (LocalizationInput) environment.getKeyContextsList().get(0);
        
        // Extract language code from input or use default
        String languageCode;
        if (input != null && input.getPriorityList() != null && !input.getPriorityList().isEmpty()) {
            languageCode = input.getPriorityList().get(0).getRange();
            // log.info("Using language from input argument: {}", languageCode);
        } else {
            languageCode = LocalizationUtils.DEFAULT_LANGUAGE_RANGE.get(0).getRange();
            // log.info("Using default language: {}", languageCode);
        }
        
        // log.info("=== BatchMapping name: Calling getNamesForMultipleIds with {} IDs, language: {} ===", 
        //          objectIds.size(), languageCode);
        
        // Load names in ONE query via CatalogService
        Map<String, String> namesById = catalogService.getNamesForMultipleIds(objectIds, languageCode);
        
        // log.info("=== BatchMapping name: Received {} names, mapping back to objects ===", namesById.size());
        
        // Map back to objects (only valid objects)
        Map<XtdObject, Optional<String>> result = validObjects.stream()
                .collect(java.util.stream.Collectors.toMap(
                        object -> object,
                        object -> Optional.ofNullable(namesById.get(object.getId()))
                ));
        
        // long duration = System.currentTimeMillis() - startTime;
        // log.info("=== BatchMapping name END: {} names for {} objects in {}ms ===", 
        //          namesById.size(), validObjects.size(), duration);
        return result;
    }

    @SchemaMapping(typeName = "XtdObject", field = "comment")
    public Optional<String> getComment(XtdObject object, @Argument LocalizationInput input) {

        XtdMultiLanguageText comment = service.getComments(object).stream().findFirst().orElse(null);
        if (comment == null) {
            return null;
        }

        XtdText translation = null;
        if (input != null && input.getPriorityList() != null) {
            translation = LocalizationUtils.getTranslation(input.getPriorityList(), comment.getId());
        } else {
            translation = LocalizationUtils.getTranslation(comment.getId());
        }

        return Optional.ofNullable(translation != null ? translation.getText() : null);
    }

}
