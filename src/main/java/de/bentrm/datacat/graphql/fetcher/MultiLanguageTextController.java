package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.service.MultiLanguageTextRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class MultiLanguageTextController {

    @Autowired
    private MultiLanguageTextRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdMultiLanguageText> getMultiLanguageText(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdMultiLanguageText.class.getSimpleName());
    }
    
    @QueryMapping
    public Connection<XtdMultiLanguageText> findMultiLanguageTexts(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdMultiLanguageText> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdMultiLanguageText", field = "texts")
    public Map<XtdMultiLanguageText, List<XtdText>> getTexts(List<XtdMultiLanguageText> multiLanguageTexts) {
        return multiLanguageTexts.stream()
                .filter(multiLanguageText -> multiLanguageText != null)  // Filter out null multiLanguageTexts
                .collect(Collectors.toMap(
                        multiLanguageText -> multiLanguageText,
                        multiLanguageText -> {
                            List<XtdText> result = service.getTexts(multiLanguageText);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }
}
