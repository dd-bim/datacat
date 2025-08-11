package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
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
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ObjectController {

    @Autowired
    private ObjectRecordService service;

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

    @SchemaMapping(typeName = "XtdObject", field = "names")
    public List<XtdMultiLanguageText> getNames(XtdObject object) {
        return service.getNames(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "dictionary")
    public Optional<XtdDictionary> getDictionary(XtdObject object) {
        return service.getDictionary(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "deprecationExplanation")
    public Optional<XtdMultiLanguageText> getDeprecationExplanation(XtdObject object) {
        return service.getDeprecationExplanation(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "replacedObjects")
    public List<XtdObject> getReplacedObjects(XtdObject object) {
        return service.getReplacedObjects(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "replacingObjects")
    public List<XtdObject> getReplacingObjects(XtdObject object) {
        return service.getReplacingObjects(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "comments")
    public List<XtdMultiLanguageText> getComments(XtdObject object) {
        return service.getComments(object);
    }

    @SchemaMapping(typeName = "XtdObject", field = "name")
    public Optional<String> getName(XtdObject object, @Argument LocalizationInput input) {

        XtdMultiLanguageText name = service.getNames(object).stream().findFirst().orElse(null);
        if (name == null) {
            return null;
        }

        XtdText translation = null;
        if (input != null && input.getPriorityList() != null) {
            translation = LocalizationUtils.getTranslation(input.getPriorityList(), name.getId());
        } else {
            translation = LocalizationUtils.getTranslation(name.getId());
        }

        return Optional.ofNullable(translation != null ? translation.getText() : null);
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
