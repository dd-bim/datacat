package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.service.CatalogService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RootController {

    @Autowired
    private CatalogService service;

    @BatchMapping(typeName = "XtdRoot", field = "recordType")
    public Map<CatalogRecord, CatalogRecordType> getRecordTypes(List<CatalogRecord> sources) {
        long startTime = System.currentTimeMillis();
        log.debug("=== BatchMapping recordType START: Processing {} sources ===", sources.size());
        
        Map<CatalogRecord, CatalogRecordType> result = sources.stream()
                .collect(java.util.stream.Collectors.toMap(
                        source -> source,
                        source -> CatalogRecordType.getByDomainClass(source)
                ));
        
        long duration = System.currentTimeMillis() - startTime;
        log.debug("=== BatchMapping recordType END: {} mappings in {}ms ===", result.size(), duration);
        return result;
    }

    @BatchMapping(typeName = "XtdRoot", field = "tags")
    public Map<CatalogRecord, List<Tag>> getTags(List<CatalogRecord> sources) {
        long startTime = System.currentTimeMillis();
        log.debug("=== BatchMapping tags START: Processing {} sources ===", sources.size());
        
        // Efficient batch loading: Load all tags for all sources in ONE query
        List<String> sourceIds = sources.stream()
                .map(CatalogRecord::getId)
                .toList();
        
        log.debug("=== BatchMapping tags: Calling getTagsForMultipleIds with {} IDs ===", sourceIds.size());
        
        // Load tags for all IDs at once
        Map<String, List<Tag>> tagsById = service.getTagsForMultipleIds(sourceIds);
        
        log.debug("=== BatchMapping tags: Received tags for {} records, mapping back to sources ===", tagsById.size());
        
        // Map back to sources
        Map<CatalogRecord, List<Tag>> result = sources.stream()
                .collect(java.util.stream.Collectors.toMap(
                        source -> source,
                        source -> tagsById.getOrDefault(source.getId(), java.util.Collections.emptyList())
                ));
        
        long duration = System.currentTimeMillis() - startTime;
        int totalTags = result.values().stream().mapToInt(List::size).sum();
        log.debug("=== BatchMapping tags END: {} tags for {} sources in {}ms ===", 
                 totalTags, result.size(), duration);
        return result;
    }
}

