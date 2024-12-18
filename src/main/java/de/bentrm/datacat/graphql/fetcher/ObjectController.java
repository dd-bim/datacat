package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ObjectController {

    @Autowired
    private ObjectRecordService objectRecordService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private ObjectRepository repository;

    @Autowired Neo4jTemplate neo4jTemplate;

    @QueryMapping
    public Optional<XtdObject> getObject(@Argument String id) {
        // return repository.findById(id);
        // return objectRecordService.findById(id);
        return objectRecordService.findByIdWithDirectRelations(id);

        // return neo4jTemplate.findById(id, XtdObject.class);
    }
    
    @QueryMapping
    public Connection<XtdObject> findObjects(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdObject> page = objectRecordService.findAll(specification);
        return Connection.of(page);
    }

    // @SchemaMapping(typeName = "XtdObject", field = "name")
    // public String getName(XtdObject object) {
    //     return "Hello World";
    // }

    @SchemaMapping(typeName = "XtdObject", field = "names")
    public List<XtdMultiLanguageText> getNames(XtdObject object) {
        return objectRecordService.getNames(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "dictionary")
    public XtdDictionary getDictionary(XtdObject object) {
        return objectRecordService.getDictionary(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "deprecationExplanation")
    public XtdMultiLanguageText getDeprecationExplanation(XtdObject object) {
        return objectRecordService.getDeprecationExplanation(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "replacedObjects")
    public List<XtdObject> getReplacedObjects(XtdObject object) {
        return objectRecordService.getReplacedObjects(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "replacingObjects")
    public List<XtdObject> getReplacingObjects(XtdObject object) {
        return objectRecordService.getReplacingObjects(object);
    }


    //     this.dictionary = environment -> {
    //         final XtdObject object = environment.getSource();
    //         return queryService.getDictionary(object);
    //     };

    //     this.deprecationExplanation = environment -> {
    //         final XtdObject object = environment.getSource();
    //         return queryService.getDeprecationExplanation(object);
    //     };

    //     this.replacedObjects = environment -> {
    //         final XtdObject object = environment.getSource();
    //         return queryService.getReplacedObjects(object);
    //     };

    //     this.replacingObjects = environment -> {
    //         final XtdObject object = environment.getSource();
    //         return queryService.getReplacingObjects(object);
    //     };

    //     // this.replacedObjects = environment -> {
    //     //     final XtdObject object = environment.getSource();
    //     //     final List<String> replacedObjectsIds = object.getReplacedObjects().stream().map(XtdObject::getId).collect(Collectors.toList());
    //     //     return catalogService.getAllObjectsById(replacedObjectsIds);
    //     // };

    //     // this.replacingObjects = environment -> {
    //     //     final XtdObject object = environment.getSource();
    //     //     final List<String> replacingObjectsIds = object.getReplacingObjects().stream().map(XtdObject::getId).collect(Collectors.toList());
    //     //     return catalogService.getAllObjectsById(replacingObjectsIds);
    //     // };

    //     this.names = environment -> {
    //         final XtdObject object = environment.getSource();
    //         return queryService.getNames(object);
    //     };
    // }

    // @Override
    // public String getFetcherName() {
    //     return "getObject";
    // }

    // @Override
    // public String getListFetcherName() {
    //     return "findObjects";
    // }

    // @Override
    // public Map<String, DataFetcher> getAttributeFetchers() {
    //     Map<String, DataFetcher> fetchers = new HashMap<>();
    //     fetchers.put("dictionary", dictionary);
    //     fetchers.put("deprecationExplanation", deprecationExplanation);
    //     fetchers.put("replacedObjects", replacedObjects);
    //     fetchers.put("replacingObjects", replacingObjects);
    //     fetchers.put("names", names);
    //     return fetchers;
    // }
}
