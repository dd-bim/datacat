package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;


@Controller
public class RootController {

    @SchemaMapping(typeName = "XtdRoot", field = "recordType")
    public CatalogRecordType recordType(CatalogRecord source) {
        return CatalogRecordType.getByDomainClass(source);
    }
}

