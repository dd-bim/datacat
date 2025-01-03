package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.*;
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
        List<List<String>> paths = new ArrayList<>();
        leaves.forEach(p -> {
            ArrayList<String> list = new ArrayList<>();
            list.add(p.id);
            paths.add(list);
        });

        return new ExportCatalogRecordsValue(leaves, paths);
    }

    @Override
    public ExportRelationshipsValue getExportCatalogRecordsRelationships() {

        List<ExportRelationshipResult> leaves = catalogExportQuery.findExportCatalogRecordsRelationships();
        List<List<String>> paths = new ArrayList<>();
        leaves.forEach(p -> {
            ArrayList<String> list = new ArrayList<>();
            list.add(p.entity1);
            paths.add(list);
        });

        return new ExportRelationshipsValue(leaves, paths);
    }
}
