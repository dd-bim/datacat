package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.service.DictionaryRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class DictionaryController {

    @Autowired
    private DictionaryRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdDictionary> getDictionary(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdDictionary.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdDictionary> findDictionaries(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdDictionary> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdDictionary", field = "name")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Map<XtdDictionary, XtdMultiLanguageText> getName(List<XtdDictionary> dictionaries) {
        return dictionaries.stream()
                .filter(dictionary -> dictionary != null)  // Filter out null dictionaries
                .collect(Collectors.toMap(
                        dictionary -> dictionary,
                        dictionary -> {
                            XtdMultiLanguageText result = service.getName(dictionary);
                            return result;  // Return as-is since it's not Optional or List
                        }
                ));                
    }

    @SchemaMapping(typeName = "XtdDictionary", field = "concepts")
    public Connection<XtdObject> getConcepts(XtdDictionary dictionary, 
                                            @Argument Integer pageSize, 
                                            @Argument Integer pageNumber) {
        if (pageSize == null) pageSize = 20;
        if (pageNumber == null) pageNumber = 0;
        
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<XtdObject> page = service.getConcepts(dictionary, pageable);
        return Connection.of(page);
    }
}
