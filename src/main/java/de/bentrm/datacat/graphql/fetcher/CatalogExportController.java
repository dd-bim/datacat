package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.service.CatalogExportService;
import de.bentrm.datacat.catalog.service.value.ExportCatalogRecordsValue;
import de.bentrm.datacat.catalog.service.value.ExportRelationshipsValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class CatalogExportController {

    @Autowired
    private CatalogExportService service;

   @QueryMapping
   public ExportCatalogRecordsValue findExportCatalogRecords() {
       return service.getExportCatalogRecords();
   }

   @QueryMapping
   public ExportRelationshipsValue findExportCatalogRecordsRelationships() {
       return service.getExportCatalogRecordsRelationships();
   }
}
