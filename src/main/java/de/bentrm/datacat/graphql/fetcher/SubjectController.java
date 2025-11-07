package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class SubjectController {

    @Autowired
    private SubjectRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdSubject> getSubject(@Argument String id) {
        long start = System.currentTimeMillis();
        Optional<XtdSubject> result = service.findByIdWithIncomingAndOutgoingRelations(id);
        long end = System.currentTimeMillis();
        log.info("getSubject executed in {} ms", end - start);
        return result;
    }

    @QueryMapping
    public Connection<XtdSubject> findSubjects(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdSubject> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdSubject", field = "properties")
    public Map<XtdSubject, List<XtdProperty>> getProperties(List<XtdSubject> subjects) {
        return subjects.stream()
                .filter(subject -> subject != null)  // Filter out null subjects
                .collect(Collectors.toMap(
                        subject -> subject,
                        subject -> {
                            List<XtdProperty> result = service.getProperties(subject);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdSubject", field = "connectedSubjects")
    public Map<XtdSubject, List<XtdRelationshipToSubject>> getConnectedSubjects(List<XtdSubject> subjects) {
        return subjects.stream()
                .filter(subject -> subject != null)  // Filter out null subjects
                .collect(Collectors.toMap(
                        subject -> subject,
                        subject -> {
                            Set<XtdRelationshipToSubject> loadedConnectedSubjects = subject.getConnectedSubjects();
                            if (loadedConnectedSubjects != null && !loadedConnectedSubjects.isEmpty()) {
                                return new ArrayList<>(loadedConnectedSubjects);
                            } else {
                                // Fallback: lade aus DB
                                List<XtdRelationshipToSubject> result = service.getConnectedSubjects(subject);
                                return result != null ? result : new ArrayList<>();  // Handle null result
                            }
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdSubject", field = "connectingSubjects")
    public Map<XtdSubject, List<XtdRelationshipToSubject>> getConnectingSubjects(List<XtdSubject> subjects) {
        return subjects.stream()
                .filter(subject -> subject != null)  // Filter out null subjects
                .collect(Collectors.toMap(
                        subject -> subject,
                        subject -> {
                            // Note: connectingSubjects werden über inverse Beziehung geladen,
                            // diese sind möglicherweise nicht direkt im Subject verfügbar
                            // Fallback zur DB-Abfrage
                            List<XtdRelationshipToSubject> result = service.getConnectingSubjects(subject);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }
}
