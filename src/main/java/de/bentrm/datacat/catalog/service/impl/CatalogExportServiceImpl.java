package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.*;
import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.service.CatalogExportService;
import de.bentrm.datacat.catalog.service.value.export.*;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Validated
@Transactional(readOnly = true)
@Service
public class CatalogExportServiceImpl implements CatalogExportService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private CatalogExportQuery catalogExportQuery;

    @Autowired
    private TranslationRespository translationRespository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CatalogRecordRepository catalogRecordRepository;

    @Autowired
    private RootRepository rootRepository;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Override
    public findExportCatalogRecordsValue getfindExportCatalogRecords() {

        List<ExportItemResult> leaves = catalogExportQuery.findExportCatalogRecords();
        List<List<String>> paths = new ArrayList<List<String>>();
        leaves.forEach(p -> {
            ArrayList<String> list = new ArrayList<String>();
            list.add(p.id);
            paths.add(list);
        });

        return new findExportCatalogRecordsValue(leaves, paths);
    }

    @Override
    public findExportCatalogRecordsRelationshipsValue getfindExportCatalogRecordsRelationships() {

        List<ExportRelationshipResult> leaves = catalogExportQuery.findExportCatalogRecordsRelationships();
        List<List<String>> paths = new ArrayList<List<String>>();
        leaves.forEach(p -> {
            ArrayList<String> list = new ArrayList<String>();
            list.add(p.entity1);
            paths.add(list);
        });

        return new findExportCatalogRecordsRelationshipsValue(leaves, paths);
    }
}
