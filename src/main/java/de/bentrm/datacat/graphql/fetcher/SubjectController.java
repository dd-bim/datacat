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
import org.springframework.graphql.data.method.annotation.SchemaMapping;
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

    // // Optimierte Schema-Mappings, die bereits geladene Daten verwenden
    // @SchemaMapping(typeName = "XtdSubject", field = "properties")
    // public List<XtdProperty> getProperties(XtdSubject subject) {
    //     Set<XtdProperty> loadedProperties = subject.getProperties();
    //     if (loadedProperties != null && !loadedProperties.isEmpty()) {
    //         return new ArrayList<>(loadedProperties);
    //     } else {
    //         // Fallback: lade aus DB
    //         return service.getProperties(subject);
    //     }
    // }

    @BatchMapping(typeName = "XtdSubject", field = "properties")
    public Map<XtdSubject, List<XtdProperty>> getProperties(List<XtdSubject> subjects) {
        return subjects.stream()
                .collect(Collectors.toMap(
                        subject -> subject,
                        subject -> service.getProperties(subject)
                ));                
    }

    @SchemaMapping(typeName = "XtdSubject", field = "connectedSubjects")
    public List<XtdRelationshipToSubject> getConnectedSubjects(XtdSubject subject) {
        Set<XtdRelationshipToSubject> loadedConnectedSubjects = subject.getConnectedSubjects();
        if (loadedConnectedSubjects != null && !loadedConnectedSubjects.isEmpty()) {
            return new ArrayList<>(loadedConnectedSubjects);
        } else {
            // Fallback: lade aus DB
            return service.getConnectedSubjects(subject);
        }
    }

    @SchemaMapping(typeName = "XtdSubject", field = "connectingSubjects")
    public List<XtdRelationshipToSubject> getConnectingSubjects(XtdSubject subject) {
        // Note: connectingSubjects werden über inverse Beziehung geladen,
        // diese sind möglicherweise nicht direkt im Subject verfügbar
        // Fallback zur DB-Abfrage
        return service.getConnectingSubjects(subject);
    }
}
