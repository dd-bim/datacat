package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.domain.XtdSubdivision;
import de.bentrm.datacat.catalog.service.CountryRecordService;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CountryController {

    @Autowired
    private CountryRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdCountry> getCountry(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdCountry.class.getSimpleName());
    }
    
    @QueryMapping
    public Connection<XtdCountry> findCountries(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdCountry> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdCountry", field = "subdivisions")
    public Map<XtdCountry, List<XtdSubdivision>> getSubdivisions(List<XtdCountry> countries) {
        return countries.stream()
                .filter(country -> country != null)  // Filter out null countries
                .collect(Collectors.toMap(
                        country -> country,
                        country -> {
                            List<XtdSubdivision> result = service.getSubdivisions(country);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }
}
