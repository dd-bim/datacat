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
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @BatchMapping(typeName = "XtdExternalDocument", field = "languages")
    public Map<XtdExternalDocument, List<XtdLanguage>> getLanguages(List<XtdExternalDocument> documents) {
        return documents.stream()
                .filter(document -> document != null)  // Filter out null documents
                .collect(Collectors.toMap(
                        document -> document,
                        document -> {
                            List<XtdLanguage> result = service.getLanguages(document);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdExternalDocument", field = "documents")
    public Map<XtdExternalDocument, List<XtdConcept>> getConcepts(List<XtdExternalDocument> documents) {
        return documents.stream()
                .filter(document -> document != null)  // Filter out null documents
                .collect(Collectors.toMap(
                        document -> document,
                        document -> {
                            List<XtdConcept> result = service.getConcepts(document);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

}
