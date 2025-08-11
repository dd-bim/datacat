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
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

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

    @SchemaMapping(typeName = "XtdSymbol", field = "subject")
    public Optional<XtdSubject> getSubject(XtdSymbol symbol) {
        return service.getSubject(symbol);
    }

    @SchemaMapping(typeName = "XtdSymbol", field = "symbol")
    public Optional<XtdText> getSymbolText(XtdSymbol symbol) {
        return service.getSymbolText(symbol);
    }
}
