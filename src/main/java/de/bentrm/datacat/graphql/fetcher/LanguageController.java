package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.service.LanguageRecordService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Slf4j
@Controller
public class LanguageController {

    @Autowired
    private LanguageRecordService languageRecordService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdLanguage> getLanguage(@Argument String id) {
        return languageRecordService.findByIdWithDirectRelations(id);
    }

    @QueryMapping
    public Optional<XtdLanguage> getLanguageByCode(@Argument String code) {
        return languageRecordService.findByCode(code);
    }

    @QueryMapping
    public Connection<XtdLanguage> findLanguages(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final QuerySpecification specification = specificationMapper.toLanguageSpecification(input);
        final Page<XtdLanguage> page = languageRecordService.findAll(specification);
        return Connection.of(page);
    }
}
