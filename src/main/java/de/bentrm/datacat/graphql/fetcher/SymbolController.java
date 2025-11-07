package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.service.SymbolRecordService;
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
public class SymbolController {

    @Autowired
    private SymbolRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdSymbol> getSymbol(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdSymbol.class.getSimpleName());
    }
    
    @QueryMapping
    public Connection<XtdSymbol> findSymbols(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdSymbol> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdSymbol", field = "subject")
    public Map<XtdSymbol, Optional<XtdSubject>> getSubject(List<XtdSymbol> symbols) {
        return symbols.stream()
                .filter(symbol -> symbol != null)  // Filter out null symbols
                .collect(Collectors.toMap(
                        symbol -> symbol,
                        symbol -> {
                            Optional<XtdSubject> result = service.getSubject(symbol);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdSymbol", field = "symbol")
    public Map<XtdSymbol, Optional<XtdText>> getSymbolText(List<XtdSymbol> symbols) {
        return symbols.stream()
                .filter(symbol -> symbol != null)  // Filter out null symbols
                .collect(Collectors.toMap(
                        symbol -> symbol,
                        symbol -> {
                            Optional<XtdText> result = service.getSymbolText(symbol);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }
}
