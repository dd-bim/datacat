package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.service.DictionaryRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class DictionaryController {

    @Autowired
    private DictionaryRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdDictionary> getDictionary(@Argument String id) {
        return service.findByIdWithDirectRelations(id);
    }

    @QueryMapping
    public Connection<XtdDictionary> findDictionaries(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdDictionary> page = service.findAll(specification);
        return Connection.of(page);
    }

    @SchemaMapping(typeName = "XtdDictionary", field = "name")
    public XtdMultiLanguageText getName(XtdDictionary dictionary) {
        return service.getName(dictionary);
    }
}
