package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.*;
import de.bentrm.datacat.dto.*;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.resolver.XtdLanguageRepresentationTypeResolver;
import de.bentrm.datacat.graphql.resolver.XtdRootTypeResolver;
import de.bentrm.datacat.service.LanguageRepresentationService;
import de.bentrm.datacat.service.XtdExternalDocumentService;
import de.bentrm.datacat.service.XtdLanguageService;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLCodeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.stream.Collectors;

import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLCodeRegistry.newCodeRegistry;

@Configuration
public class XtdDataFetchers {

    @Autowired
    private LanguageRepresentationService languageRepresentationService;

    @Autowired
    private XtdLanguageService languageService;

    @Autowired
    private XtdExternalDocumentService externalDocumentService;

    @Bean("xtdCodeRegistry")
    public GraphQLCodeRegistry buildCodeRegistry() {
        return newCodeRegistry()
                .typeResolver(XtdLanguageRepresentation.LABEL, new XtdLanguageRepresentationTypeResolver())
                .typeResolver(XtdRoot.LABEL, new XtdRootTypeResolver())
                .dataFetcher(coordinates("mutation", "addName"), addName())
                .dataFetcher(coordinates("mutation", "updateName"), updateName())
                .dataFetcher(coordinates("mutation", "deleteName"), deleteName())
                .dataFetcher(coordinates("mutation", "addDescription"), addDescription())
                .dataFetcher(coordinates("mutation", "updateDescription"), updateDescription())
                .dataFetcher(coordinates("mutation", "deleteDescription"), deleteDescription())
                .dataFetcher(coordinates("mutation", "addLanguage"), addLanguage())
                .dataFetcher(coordinates("query", "language"), languageByUniqueId())
                .dataFetcher(coordinates("query", "languages"), languageBySearch())
                .dataFetcher(coordinates(XtdName.LABEL, "languageName"), getLanguage())
                .dataFetcher(coordinates(XtdDescription.LABEL, "languageName"), getLanguage())
                .dataFetcher(coordinates("mutation", "addDocument"), addExternalDocument())
                .dataFetcher(coordinates("mutation", "deleteDocument"), deleteExternalDocument())
                .dataFetcher(coordinates("query", "document"), externalDocumentByUniqueId())
                .dataFetcher(coordinates("query", "documents"), externalDocumentBySearch())
                .build();
    }

    private DataFetcher<XtdLanguage> addLanguage() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("newLanguage");
            ObjectMapper mapper = new ObjectMapper();
            XtdLanguageInputDto dto = mapper.convertValue(input, XtdLanguageInputDto.class);
            return languageService.create(dto);
        };
    }

    private DataFetcher<XtdLanguage> languageByUniqueId() {
        return environment -> {
            String uniqueId = environment.getArgument("uniqueId");
            return languageService.findByUniqueId(uniqueId);
        };
    }

    private DataFetcher<Connection<XtdLanguage>> languageBySearch() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");

            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) dto = SearchOptionsDto.defaults();

            Page<XtdLanguage> page = languageService.findAll(dto.getPageNumber(), dto.getPageSize());
            Connection<XtdLanguage> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
        };
    }

    private DataFetcher<XtdLanguage> getLanguage() {
        return environment -> {
            XtdLanguageRepresentation value = environment.getSource();
            return languageService.findByLanguageRepresentationUniqueId(value.getUniqueId());
        };
    }

    private DataFetcher<XtdObject> addName() {
        return environment -> {
            String parentUniqueId = environment.getArgument("parentUniqueId");
            Map<String, Object> input = environment.getArgument("newName");
            ObjectMapper mapper = new ObjectMapper();
            XtdNameInputDto dto = mapper.convertValue(input, XtdNameInputDto.class);
            return languageRepresentationService.addName(parentUniqueId, dto);
        };
    }

    private DataFetcher<XtdObject> updateName() {
        return environment -> {
            String parentUniqueId = environment.getArgument("parentUniqueId");
            String uniqueId = environment.getArgument("uniqueId");
            String newName = environment.getArgument("newName");
            return languageRepresentationService.updateName(parentUniqueId, uniqueId, newName);
        };
    }

    private DataFetcher<XtdObject> deleteName() {
        return environment -> {
            String parentUniqueId = environment.getArgument("parentUniqueId");
            String uniqueId = environment.getArgument("uniqueId");
            return languageRepresentationService.deleteName(parentUniqueId, uniqueId);
        };
    }

    private DataFetcher<XtdObject> addDescription() {
        return environment -> {
            String parentUniqueId = environment.getArgument("parentUniqueId");
            Map<String, Object> input = environment.getArgument("newDescription");
            ObjectMapper mapper = new ObjectMapper();
            XtdDescriptionInputDto dto = mapper.convertValue(input, XtdDescriptionInputDto.class);
            return languageRepresentationService.addDescription(parentUniqueId, dto);
        };
    }

    private DataFetcher<XtdObject> updateDescription() {
        return environment -> {
            String parentUniqueId = environment.getArgument("parentUniqueId");
            String uniqueId = environment.getArgument("uniqueId");
            String newDescription = environment.getArgument("newDescription");
            return languageRepresentationService.updateDescription(parentUniqueId, uniqueId, newDescription);
        };
    }

    private DataFetcher<XtdObject> deleteDescription() {
        return environment -> {
            String parentUniqueId = environment.getArgument("parentUniqueId");
            String uniqueId = environment.getArgument("uniqueId");
            return languageRepresentationService.deleteDescription(parentUniqueId, uniqueId);
        };
    }

    private DataFetcher<XtdExternalDocument> addExternalDocument() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("newDocument");
            ObjectMapper mapper = new ObjectMapper();
            XtdExternalDocumentInputDto dto = mapper.convertValue(input, XtdExternalDocumentInputDto.class);
            return externalDocumentService.createExternalDocument(dto);
        };
    }

    private DataFetcher<XtdExternalDocument> deleteExternalDocument() {
        return environment -> {
            String uniqueId = environment.getArgument("uniqueId");
            return externalDocumentService.deleteExternalDocument(uniqueId);
        };
    }

    private DataFetcher<XtdExternalDocument> externalDocumentByUniqueId() {
        return environment -> {
            String uniqueId = environment.getArgument("uniqueId");
            return externalDocumentService.findByUniqueId(uniqueId);
        };
    }

    private DataFetcher<Connection<XtdExternalDocument>> externalDocumentBySearch() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) {
                dto = SearchOptionsDto.defaults();
            }

            Page<XtdExternalDocument> page;
            if (dto.hasTerm()) {
                page = externalDocumentService.findExternalDocumentsByTerm(dto.getTerm(), dto.getPageNumber(), dto.getPageSize());
            } else {
                page = externalDocumentService.findAllExternalDocuments(dto.getPageNumber(), dto.getPageSize());
            }

            Connection<XtdExternalDocument> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));

            return connection;
        };
    }

}
