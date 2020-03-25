package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.domain.XtdLanguageRepresentation;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.ExternalDocumentInput;
import de.bentrm.datacat.graphql.dto.PagingOptions;
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

    public DataFetcher<Optional<XtdLanguage>> languageByLanguageRepresentation() {
        return environment -> {
            XtdLanguageRepresentation value = environment.getSource();
            return languageService.findByLanguage(value.getLanguageName());
        };
    }

    public DataFetcher<XtdExternalDocument> createExternalDocument() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            ExternalDocumentInput dto = mapper.convertValue(input, ExternalDocumentInput.class);
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
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdExternalDocument> page;
            String term = environment.getArgument("term");
            if (term != null && !term.isBlank()) {
                page = externalDocumentService.findByTerm(term.trim(), dto.getPageble());
            } else {
                page = externalDocumentService.findAll(dto.getPageble());
            }

            return new Connection<>(page);
        };
    }

}
