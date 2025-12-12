package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdRelationshipType;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.service.RelationshipToSubjectRecordService;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.input.RelationshipToSubjectFilterInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class SubjectController {

    @Autowired
    private SubjectRecordService service;
    
    @Autowired
    private RelationshipToSubjectRecordService relationshipService;
    
    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdSubject> getSubject(@Argument String id) {
        return service.findById(id);
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

    @SchemaMapping(typeName = "XtdSubject", field = "connectedSubjects")
    public List<XtdRelationshipToSubject> getConnectedSubjects(
            XtdSubject subject,
            @Argument RelationshipToSubjectFilterInput filter) {
        
        // Lade connectedSubjects explizit aus der DB
        List<XtdRelationshipToSubject> relationships = service.getConnectedSubjects(subject);
        if (relationships == null) {
            relationships = new ArrayList<>();
        }
        
        return filterRelationshipsByTypeName(relationships, filter);
    }

    @SchemaMapping(typeName = "XtdSubject", field = "connectingSubjects")
    public List<XtdRelationshipToSubject> getConnectingSubjects(
            XtdSubject subject,
            @Argument RelationshipToSubjectFilterInput filter) {
        
        // connectingSubjects werden über inverse Beziehung geladen,
        // diese sind möglicherweise nicht direkt im Subject verfügbar
        // Fallback zur DB-Abfrage
        List<XtdRelationshipToSubject> relationships = service.getConnectingSubjects(subject);
        if (relationships == null) {
            relationships = new ArrayList<>();
        }
        
        return filterRelationshipsByTypeName(relationships, filter);
    }
    
    /**
     * Filtert eine Liste von XtdRelationshipToSubject nach dem relationshipType Namen.
     * 
     * @param relationships Die zu filternden Relationships
     * @param filter Der Filter mit dem relationshipTypeName
     * @return Die gefilterte Liste von Relationships
     */
    private List<XtdRelationshipToSubject> filterRelationshipsByTypeName(
            List<XtdRelationshipToSubject> relationships,
            RelationshipToSubjectFilterInput filter) {
        
        // Filter anwenden, falls vorhanden
        if (filter != null && filter.getRelationshipTypeName() != null) {
            String filterName = filter.getRelationshipTypeName();
            
            relationships = relationships.stream()
                .filter(rel -> {
                    // Lade relationshipType explizit, falls noch nicht geladen
                    XtdRelationshipType type = rel.getRelationshipType();
                    if (type == null) {
                        type = relationshipService.getRelationshipType(rel);
                    }
                    
                    if (type == null) {
                        return false;
                    }
                    
                    // Lade den Type mit allen Namen mit Neo4jTemplate
                    XtdRelationshipType fullType = neo4jTemplate.findById(type.getId(), XtdRelationshipType.class).orElse(null);
                    if (fullType != null) {
                        type = fullType;
                    }
                    
                    // Get the name from the type's names collection
                    if (type.getNames() == null || type.getNames().isEmpty()) {
                        return false;
                    }
                    
                    // Check if any text matches the filter
                    return type.getNames().stream()
                        .filter(nameText -> nameText != null && nameText.getTexts() != null)
                        .flatMap(nameText -> nameText.getTexts().stream())
                        .anyMatch(text -> text.getText() != null && text.getText().equals(filterName));
                })
                .collect(Collectors.toList());
        }
        
        return relationships;
    }
}
