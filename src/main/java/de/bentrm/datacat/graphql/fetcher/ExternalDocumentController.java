package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.service.ExternalDocumentRecordService;
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
import java.util.List;

@Controller
public class ExternalDocumentController {

    @Autowired
    private ExternalDocumentRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdExternalDocument> getExternalDocument(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdExternalDocument.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdExternalDocument> findExternalDocuments(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdExternalDocument> page = service.findAll(specification);
        return Connection.of(page);
    }

    @SchemaMapping(typeName = "XtdExternalDocument", field = "languages")
    public List<XtdLanguage> getLanguages(XtdExternalDocument document) {
        return service.getLanguages(document);
    }

    @SchemaMapping(typeName = "XtdExternalDocument", field = "documents")
    public List<XtdConcept> getConcepts(XtdExternalDocument document) {
        return service.getConcepts(document);
    }

}
