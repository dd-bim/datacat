package de.bentrm.datacat;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.repository.*;
import de.bentrm.datacat.service.AdminService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

// TODO: Refactor import logic into separate class using application scoped services
//@Component
public class DataImporter implements ResourceLoaderAware {

    protected Logger logger = LoggerFactory.getLogger(DataImporter.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private ExternalDocumentRepository externalDocumentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private BagRepository bagRepository;

    @Autowired
    private RelDocumentsRepository relDocumentsRepository;

    @Autowired
    private RelCollectsRepository relCollectsRepository;

    @Autowired
    private RelGroupsRepository relGroupsRepository;

    private ResourceLoader resourceLoader;

    @Value("${import.main}")
    private String importFilePath;

    @Value("${import.descriptions}")
    private String descriptionFilePath;

    private Map<String, String> entityIds = new HashMap<>();


//    @Bean
    @Transactional
    public CommandLineRunner importInitialData() {
        return (args) -> {
            adminService.purgeDatabase();

            if (importFilePath == null) {
                logger.info("No import file provided..");
                return;
            }

            if (descriptionFilePath == null) {
                logger.info("No descriptions file provided..");
                return;
            }

            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            mapper.enable(CsvParser.Feature.TRIM_SPACES);
            CsvSchema columns = mapper.schemaFor(String[].class).withColumnSeparator(';');
            ObjectReader reader = mapper.readerFor(String[].class).with(columns);

            List<String[]> entities = new ArrayList<>();
            List<String[]> descriptions = new ArrayList<>();

            try {
                Resource importFile = resourceLoader.getResource("file://" + importFilePath);
                logger.info("Import file: " + importFile.getURI());
                MappingIterator<String[]> importIterator = reader.readValues(importFile.getInputStream());
                importIterator.forEachRemaining(entities::add);
            } catch (IOException e) {
                logger.error("Import file not found..", e);
                return;
            }

            try {
                Resource descFile = resourceLoader.getResource("file://" + descriptionFilePath);
                logger.info("Import file: " + descFile.getURI());
                MappingIterator<String[]> importIterator = reader.readValues(descFile.getInputStream());
                importIterator.forEachRemaining(descriptions::add);
            } catch (IOException e) {
                logger.error("Descriptions file not found..", e);
                return;
            }

            importCollectionNodes(entities);
            importGroupNodes(entities);
            importSubjectNodes(entities);
            importSubjectDescriptions(descriptions);
            importDocuments();
            importCollects(entities);
            importGroups(entities);
        };
    }

    @Transactional
    protected  void importCollectionNodes(List<String[]> rows) {
        var lineNumber = 0;

        for (String[] properties : rows) {
            if (lineNumber > 0) {
                String names = properties[0];

                // map "Fachmodell" to Collection
                if (!names.isBlank() && !entityIds.containsKey(names)) {
                    var newBag = new XtdBag();
                    String[] split = names.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String name = split[i];
                        XtdName newName = new XtdName();
                        newName.setLanguageName("de");
                        newName.setName(name);
                        newName.setSortOrder(i);
                        newBag.getNames().add(newName);
                    }

                    newBag = bagRepository.save(newBag);
                    entityIds.put(names, newBag.getId());
                }

            }
            lineNumber++;
        }

        logger.info("Imported collection nodes..");
    }

    @Transactional
    protected void importGroupNodes(List<String[]> rows)  {
        var lineNumber = 0;

        for (String[] properties : rows) {
            if (lineNumber > 0) {
                String names = properties[1];

                if (!names.isBlank() && !entityIds.containsKey(names)) {
                    var newSubject = new XtdSubject();
                    String[] split = names.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String name = split[i];
                        XtdName newName = new XtdName();
                        newName.setLanguageName("de");
                        newName.setName(name);
                        newName.setSortOrder(i);
                        newSubject.getNames().add(newName);
                    }

                    newSubject = subjectRepository.save(newSubject);
                    entityIds.put(names, newSubject.getId());
                }

            }
            lineNumber++;
        }
        logger.info("Imported group nodes..");
    }

    @Transactional
    void importSubjectNodes(List<String[]> rows) {
        var lineNumber = 0;

        for (String[] properties : rows) {
            if (lineNumber > 0) {
                String names = properties[2];

                if (!names.isBlank() && !entityIds.containsKey(names)) {
                    var newSubject = new XtdSubject();
                    String[] split = names.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String name = split[i];
                        XtdName newName = new XtdName();
                        newName.setLanguageName("de");
                        newName.setName(name);
                        newName.setSortOrder(i);
                        newSubject.getNames().add(newName);
                    }

                    newSubject = subjectRepository.save(newSubject);
                    entityIds.put(names, newSubject.getId());
                }
            }
            lineNumber++;
        }
        logger.info("Imported subject nodes..");
    }


    @Transactional
    protected void importSubjectDescriptions(List<String[]> rows) {

        int lineNumber = 0;
        for (String[] line : rows) {
            if (lineNumber > 0) {
                var subjectName = line[0];
                var desc = line[2];

                if (subjectName.isBlank() || desc.isBlank()) {
                    continue;
                }

                String subjectUid = entityIds.get(subjectName);
                var persistentSubject = subjectRepository.findById(subjectUid);
                persistentSubject.ifPresentOrElse(subject -> {
                    var descriptionEntity = new XtdDescription();
                    descriptionEntity.setLanguageName("de");
                    descriptionEntity.setDescription(desc);
                    subject.addDescription(descriptionEntity);
                    subjectRepository.save(subject);
                }, () -> logger.info("No subject found with name: " + subjectName));
            }
            lineNumber++;
        }
        logger.info("Imported subject descriptions..");
    }

    @Transactional
    protected void importDocuments() {
        var buildingSmart = new XtdExternalDocument();
        var documentName = new XtdName();
        documentName.setName("BuildingSMART FG");
        documentName.setLanguageName("de");
        buildingSmart.getNames().add(documentName);

        externalDocumentRepository.save(buildingSmart);

        var relationship = new XtdRelDocuments();
        var relationshipName = new XtdName();
        relationshipName.setName("BuildingSMART FG");
        relationshipName.setLanguageName("de");
        relationship.getNames().add(relationshipName);
        relationship.setRelatingDocument(buildingSmart);
        subjectRepository.findAll(PageRequest.of(1, 1000)).forEach(subject ->relationship.getRelatedThings().add(subject));
        relDocumentsRepository.save(relationship);

        logger.info("Imported documents..");
    }

    @Transactional
    protected void importCollects(List<String[]> rows) {
        var lineNumber = 0;
        String curRelatingCollectionName = null;
        String curRelatedThing = null;

        XtdRelCollects relationship = null;

        for (String[] properties : rows) {
            if (lineNumber > 0) {
                String bagName = properties[0];
                String subjectName = properties[1];

                if (!bagName.isBlank() && !bagName.equals(curRelatingCollectionName)) {
                    curRelatingCollectionName = bagName;

                    if (relationship != null) {
                        relCollectsRepository.save(relationship);
                    }

                    relationship = new XtdRelCollects();
                    String collectionId = entityIds.get((curRelatingCollectionName));
                    Optional<XtdBag> currentBag = bagRepository.findById(collectionId);
                    relationship.getNames().add(new XtdName("de", curRelatingCollectionName));
                    relationship.setRelatingCollection(currentBag.get());
                }

                if (!subjectName.isBlank() && !subjectName.equals(curRelatedThing)) {
                    curRelatedThing = subjectName;

                    var subject = subjectRepository.findById(entityIds.get(curRelatedThing));
                    relationship.getRelatedThings().add(subject.get());
                }
            }
            lineNumber++;
        }

        if (relationship != null) {
            relCollectsRepository.save(relationship);
        }

        logger.info("Imported collects relationships..");
    }

    @Transactional
    protected void importGroups(List<String[]> rows) {
        var lineNumber = 0;
        String curGroupName = null;
        String curGroupMemberName = null;

        XtdRelGroups relationship = null;

        for (String[] properties : rows) {
            if (lineNumber > 0) {
                String groupName = properties[1];
                String subjectName = properties[2];

                if (!groupName.isBlank() && !groupName.equals(curGroupName)) {
                    curGroupName = groupName;

                    if (relationship != null) {
                        relGroupsRepository.save(relationship);
                    }

                    String groupUid = entityIds.get((curGroupName));
                    Optional<XtdSubject> subject = subjectRepository.findById(groupUid);
                    var relatingObject = subject.get();
                    if (!relatingObject.getGroups().isEmpty()) {
                        relationship = relatingObject.getGroups().iterator().next();
                    } else {
                        relationship = new XtdRelGroups();
                        relationship.getNames().add(new XtdName("de", curGroupName));
                        relationship.setRelatingThing(relatingObject);
                    }

                }

                if (!subjectName.isBlank() && !subjectName.equals(curGroupName) && !subjectName.equals(curGroupMemberName)) {
                    curGroupMemberName = subjectName;

                    var subject = subjectRepository.findById(entityIds.get(curGroupMemberName));
                    relationship.getRelatedThings().add(subject.get());
                }
            }
            lineNumber++;
        }

        if (relationship != null) {
            relGroupsRepository.save(relationship);
        }

        logger.info("Imported groups relationships..");
    }

    @Override
    public void setResourceLoader(@NotNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
