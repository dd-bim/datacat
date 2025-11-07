package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.CatalogSearchService;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.input.HierarchyFilterInput;
import de.bentrm.datacat.graphql.input.HierarchyRootNodeFilterInput;
import de.bentrm.datacat.graphql.input.SearchInput;
import graphql.schema.DataFetchingFieldSelectionSet;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class SearchController {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CatalogSearchService catalogSearchService;

    @QueryMapping
    public Connection<XtdRoot> search(@Argument SearchInput input, @Argument Integer pageSize, @Argument Integer pageNumber, DataFetchingFieldSelectionSet selectionSet) {

        if (input == null) input = new SearchInput();
        if (pageSize != null) input.setPageSize(pageSize);
        if (pageNumber != null) input.setPageNumber(pageNumber);

        CatalogRecordSpecification spec = specificationMapper.toCatalogRecordSpecification(input);

        if (selectionSet.containsAnyOf("nodes/*", "pageInfo/*")) {
                Page<XtdRoot> page = catalogSearchService.search(spec);
                return Connection.of(page);
            } else {
                Long totalElements = catalogSearchService.count(spec);
                return Connection.empty(totalElements);
            }
    }

    @QueryMapping
    public HierarchyValue hierarchy(@Argument HierarchyFilterInput input) {
        log.info("=== Hierarchy endpoint called with input: {} ===", input);
        
        try {
            if (input == null) {
                log.warn("Input is null, creating empty HierarchyFilterInput");
                input = new HierarchyFilterInput();
            }
            
            final HierarchyRootNodeFilterInput rootNodeFilter = input.getRootNodeFilter();
            log.info("Root node filter: {}", rootNodeFilter);
            
            final CatalogRecordSpecification rootNodeSpecification = specificationMapper.toCatalogRecordSpecification(rootNodeFilter);
            log.info("Root node specification: {}", rootNodeSpecification);
            
            HierarchyValue result = catalogService.getHierarchy(rootNodeSpecification);
            log.info("=== Hierarchy result: {} nodes, {} paths ===", 
                    result.getNodes().size(), result.getPaths().size());
            
            return result;
        } catch (Exception e) {
            log.error("Error in hierarchy endpoint", e);
            throw e;
        }
    }
}
