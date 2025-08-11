package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.ExportItemResult;
import de.bentrm.datacat.catalog.domain.ExportRelationshipResult;
import de.bentrm.datacat.catalog.repository.CatalogExportQuery;
import de.bentrm.datacat.catalog.service.CatalogExportService;
import de.bentrm.datacat.catalog.service.value.ExportCatalogRecordsValue;
import de.bentrm.datacat.catalog.service.value.ExportRelationshipsValue;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Slf4j
@Validated
@Transactional(readOnly = true)
@Service
public class CatalogExportServiceImpl implements CatalogExportService {

    @Autowired
    private CatalogExportQuery catalogExportQuery;

    @Override
    public ExportCatalogRecordsValue getExportCatalogRecords() {
        List<ExportItemResult> leaves = catalogExportQuery.findExportCatalogRecords();
        return new ExportCatalogRecordsValue(leaves);
    }

    @Override
    public ExportRelationshipsValue getExportCatalogRecordsRelationships() {
        List<ExportRelationshipResult> leaves = catalogExportQuery.findExportCatalogRecordsRelationships();
        return new ExportRelationshipsValue(leaves);
    }
    
}
