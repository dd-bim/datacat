package de.bentrm.datacat;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.bentrm.datacat.domain.*;
import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.repository.ExternalDocumentRepository;
import de.bentrm.datacat.repository.LanguageRepository;
import de.bentrm.datacat.repository.SubjectRepository;
import de.bentrm.datacat.repository.collection.BagRepository;
import de.bentrm.datacat.repository.relationship.RelCollectsRepository;
import de.bentrm.datacat.repository.relationship.RelDocumentsRepository;
import de.bentrm.datacat.repository.relationship.RelGroupsRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Refactor import logic into separate class using application scoped services
@Component
public class DataImporter implements ResourceLoaderAware {

    protected Log logger = LogFactory.getLog(DataImporter.class);

    @Autowired private LanguageRepository languageRepository;
    @Autowired private ExternalDocumentRepository externalDocumentRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private BagRepository bagRepository;
    @Autowired private RelDocumentsRepository relDocumentsRepository;
    @Autowired private RelCollectsRepository relCollectsRepository;
    @Autowired private RelGroupsRepository relGroupsRepository;

    private ResourceLoader resourceLoader;

    @Value("${import.main}")
    private String importFilePath;

    @Value("${import.descriptions}")
    private String descriptionFilePath;

    private Map<String, String> entityIds = new HashMap<>();


    @Bean
    @Transactional
    public CommandLineRunner importInitialData() {
        return (args) -> {
            pruneDatabase();
            initAdminData();

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
    protected void pruneDatabase() {
        languageRepository.pruneDatabase();
        logger.info("Pruning database..");
    }

    @Transactional
    protected void initAdminData() {
        var german = new XtdLanguage();
        german.setLanguageCode("de");
        german.setLanguageNameInEnglish("German");
        german.setLanguageNameInSelf("Deutsch");
        languageRepository.save(german);
        logger.info("Initializing admin data..");
    }

    @Transactional
    protected  void importCollectionNodes(List<String[]> rows) {
        var lineNumber = 0;
        var german = languageRepository.findByLanguageCode("de");

        for (String[] properties : rows) {
            if (lineNumber > 0) {
                String names = properties[0];

                // map "Fachmodell" to Collection
                if (!names.isBlank()) {
                    XtdBag newBag = bagRepository.findById(entityIds.get(names));

                    if (newBag != null) {
                        continue;
                    }

                    newBag = new XtdBag();
                    String[] split = names.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String name = split[i];
                        XtdName newName = new XtdName();
                        newName.setLanguageName(german);
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
        var german = languageRepository.findByLanguageCode("de");

        for (String[] properties : rows) {
            if (lineNumber > 0) {
                String names = properties[1];

                if (!names.isBlank()) {
                    XtdSubject newSubject = subjectRepository.findById(entityIds.get(names));

                    if (newSubject != null) {
                        continue;
                    }

                    newSubject = new XtdSubject();

                    String[] split = names.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String name = split[i];
                        XtdName newName = new XtdName();
                        newName.setLanguageName(german);
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
        var german = languageRepository.findByLanguageCode("de");
        var lineNumber = 0;

        for (String[] properties : rows) {
            if (lineNumber > 0) {
                String names = properties[2];

                if (!names.isBlank()) {
                    XtdSubject newSubject = subjectRepository.findById(entityIds.get(names));

                    if (newSubject != null) {
                        continue;
                    }

                    newSubject = new XtdSubject();
                    String[] split = names.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String name = split[i];
                        XtdName newName = new XtdName();
                        newName.setLanguageName(german);
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
        var german = languageRepository.findByLanguageCode("de");

        int lineNumber = 0;
        for (String[] line : rows) {
            if (lineNumber > 0) {
                var subjectName = line[0];
                var desc = line[2];

                if (subjectName.isBlank() || desc.isBlank()) {
                    continue;
                }

                var subject = subjectRepository.findById(entityIds.get(subjectName));
                if (subject == null) {
                    System.out.println("No subject found with name: " + subjectName);
                    continue;
                }

                var descriptionEntity = new XtdDescription();
                descriptionEntity.setLanguageName(german);
                descriptionEntity.setDescription(desc);
                subject.addDescription(descriptionEntity);
                subjectRepository.save(subject);
            }
            lineNumber++;
        }
        logger.info("Imported subject descriptions..");
    }

    @Transactional
    protected void importDocuments() {
        var german = languageRepository.findByLanguageCode("de");
        var buildingSmart = new XtdExternalDocument();
        var name = new XtdName();
        name.setName("BuildingSMART FG");
        name.setLanguageName(german);
        buildingSmart.getNames().add(name);

        externalDocumentRepository.save(buildingSmart);

        var relationship = new XtdRelDocuments();
        relationship.setRelatingDocument(buildingSmart);
        subjectRepository.findAll().forEach(subject ->relationship.getRelatedThings().add(subject));
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
                    relationship.setRelatingCollection(bagRepository.findById(entityIds.get((curRelatingCollectionName))));
                }

                if (!subjectName.isBlank() && !subjectName.equals(curRelatedThing)) {
                    curRelatedThing = subjectName;

                    var subject = subjectRepository.findById(entityIds.get(curRelatedThing));
                    relationship.getRelatedThings().add(subject);
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

                    var relatingObject = subjectRepository.findById(entityIds.get((curGroupName)));
                    if (!relatingObject.getAssociates().isEmpty()) {
                        relationship = (XtdRelGroups) relatingObject.getAssociates().iterator().next();
                    } else {
                        relationship = new XtdRelGroups();
                        relationship.setRelatingObject(relatingObject);
                    }

                }

                if (!subjectName.isBlank() && !subjectName.equals(curGroupName) && !subjectName.equals(curGroupMemberName)) {
                    curGroupMemberName = subjectName;

                    var subject = subjectRepository.findById(entityIds.get(curGroupMemberName));
                    relationship.getRelatedObjects().add(subject);
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
