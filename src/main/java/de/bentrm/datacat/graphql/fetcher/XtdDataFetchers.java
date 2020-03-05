package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.domain.XtdLanguageRepresentation;
import de.bentrm.datacat.dto.ExternalDocumentInputDto;
import de.bentrm.datacat.dto.LanguageInputDto;
import de.bentrm.datacat.dto.SearchOptionsDto;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.service.ExternalDocumentService;
import de.bentrm.datacat.service.LanguageService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class XtdDataFetchers {

    @Autowired
    private LanguageService languageService;

    @Autowired
    private ExternalDocumentService externalDocumentService;

    public DataFetcher<XtdLanguage> createLanguage() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("language");
            ObjectMapper mapper = new ObjectMapper();
            LanguageInputDto dto = mapper.convertValue(input, LanguageInputDto.class);
            return languageService.create(dto);
        };
    }

    public DataFetcher<Optional<XtdLanguage>> deleteLanguage() {
        return environment -> {
            String id = environment.getArgument("id");
            return languageService.delete(id);
        };
    }

    public DataFetcher<Optional<XtdLanguage>> languageById() {
        return environment -> {
            String id = environment.getArgument("id");
            return languageService.findById(id);
        };
    }

    public DataFetcher<Connection<XtdLanguage>> languageBySearch() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");

            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) dto = SearchOptionsDto.defaults();

            Page<XtdLanguage> page = languageService.findAll(dto.getPageble());
            return new Connection<>(page);
        };
    }

    public DataFetcher<Optional<XtdLanguage>> languageByLanguageRepresentationId() {
        return environment -> {
            XtdLanguageRepresentation value = environment.getSource();
            return languageService.findByLanguageRepresentationId(value.getId());
        };
    }

    public DataFetcher<XtdExternalDocument> createExternalDocument() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("document");
            ObjectMapper mapper = new ObjectMapper();
            ExternalDocumentInputDto dto = mapper.convertValue(input, ExternalDocumentInputDto.class);
            return externalDocumentService.create(dto);
        };
    }

    public DataFetcher<Optional<XtdExternalDocument>> deleteExternalDocument() {
        return environment -> {
            String id = environment.getArgument("id");
            return externalDocumentService.delete(id);
        };
    }

    public DataFetcher<Optional<XtdExternalDocument>> externalDocumentById() {
        return environment -> {
            String id = environment.getArgument("id");
            return externalDocumentService.findById(id);
        };
    }

    public DataFetcher<Connection<XtdExternalDocument>> externalDocumentBySearch() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) {
                dto = SearchOptionsDto.defaults();
            }

            Page<XtdExternalDocument> page;
            if (dto.hasTerm()) {
                page = externalDocumentService.findByTerm(dto.getTerm(), dto.getPageble());
            } else {
                page = externalDocumentService.findAll(dto.getPageble());
            }

            return new Connection<>(page);
        };
    }

}
