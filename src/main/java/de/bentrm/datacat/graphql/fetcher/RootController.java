package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.service.CatalogService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;


@Controller
public class RootController {

    @Autowired
    private CatalogService service;

    @SchemaMapping(typeName = "XtdRoot", field = "recordType")
    public CatalogRecordType recordType(CatalogRecord source) {
        return CatalogRecordType.getByDomainClass(source);
    }

    @SchemaMapping(typeName = "XtdRoot", field = "tags") 
    public List<Tag> getTags(CatalogRecord source) {
        return service.getTags(source.getId());
    }
}

