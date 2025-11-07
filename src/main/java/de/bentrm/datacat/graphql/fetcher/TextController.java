package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.service.TextRecordService;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class TextController {

    @Autowired
    private TextRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdText> getText(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdText.class.getSimpleName());
    }
    
    @QueryMapping
    public Connection<XtdText> findTexts(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdText> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdText", field = "language")
    public Map<XtdText, XtdLanguage> getLanguage(List<XtdText> texts) {
        return texts.stream()
                .filter(text -> text != null)  // Filter out null texts
                .collect(Collectors.toMap(
                        text -> text,
                        text -> {
                            XtdLanguage result = service.getLanguage(text);
                            return result;  // Return as-is since it's not Optional or List
                        }
                ));                
    }
}
