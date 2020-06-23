package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.*;
import de.bentrm.datacat.service.RelDocumentsService;
import de.bentrm.datacat.service.Specification;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RelDocumentsDataFetcherProvider implements QueryDataFetcherProvider, ObjectDataFetcherProvider, MutationDataFetcherProvider {

    @Autowired
    private RelDocumentsService relDocumentsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("documentsRelations", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getObjectDataFetchers() {
        return Map.ofEntries(
                Map.entry("documentedBy", documentedBy())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createDocumentsRelation", add()),
                Map.entry("updateDocumentsRelation", update()),
                Map.entry("deleteDocumentsRelation", remove())
        );
    }

    public DataFetcher<XtdRelDocuments> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            DocumentsInput dto = objectMapper.convertValue(input, DocumentsInput.class);
            return relDocumentsService.create(dto);
        };
    }

    public DataFetcher<XtdRelDocuments> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            DocumentsUpdateInput dto = objectMapper.convertValue(input, DocumentsUpdateInput.class);
            return relDocumentsService.update(dto);
        };
    }

    public DataFetcher<Optional<XtdRelDocuments>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return relDocumentsService.delete(id);
        };
    }

    public DataFetcher<Connection<XtdRelDocuments>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            FilterInput filterInput = objectMapper.convertValue(input, FilterInput.class);
            if (filterInput == null) filterInput = new FilterInput();

            Specification spec = DtoMapper.INSTANCE.toSpecification(filterInput);
            Page<XtdRelDocuments> page = relDocumentsService.search(spec);
            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelDocuments>> collects() {
        return environment -> {
            XtdExternalDocument collection = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = collection.getDocuments().stream().map(XtdRelDocuments::getId).collect(Collectors.toList());
            Page<XtdRelDocuments> page = relDocumentsService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }

    public DataFetcher<Connection<XtdRelDocuments>> documentedBy() {
        return environment -> {
            XtdObject source = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = objectMapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = source.getDocumentedBy().stream().map(XtdRelDocuments::getId).collect(Collectors.toList());
            Page<XtdRelDocuments> page = relDocumentsService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }
}
