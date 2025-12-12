package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdRelationshipType;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.service.RelationshipToSubjectRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
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
import java.util.stream.Collectors;

@Controller
public class RelationshipToSubjectController {

    @Autowired
    private RelationshipToSubjectRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdRelationshipToSubject> getRelationshipToSubject(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdRelationshipToSubject.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdRelationshipToSubject> findRelationshipToSubjects(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdRelationshipToSubject> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdRelationshipToSubject", field = "connectingSubject")
    public Map<XtdRelationshipToSubject, XtdSubject> getConnectingSubject(List<XtdRelationshipToSubject> relationshipToSubjects) {
        return relationshipToSubjects.stream()
                .filter(relationshipToSubject -> relationshipToSubject != null)  // Filter out null relationships
                .collect(Collectors.toMap(
                        relationshipToSubject -> relationshipToSubject,
                        relationshipToSubject -> {
                            XtdSubject result = service.getConnectingSubject(relationshipToSubject);
                            return result;  // Return as-is since it's not Optional or List
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdRelationshipToSubject", field = "targetSubjects")
    public Map<XtdRelationshipToSubject, List<XtdSubject>> getTargetSubjects(List<XtdRelationshipToSubject> relationshipToSubjects) {
        return relationshipToSubjects.stream()
                .filter(relationshipToSubject -> relationshipToSubject != null)  // Filter out null relationships
                .collect(Collectors.toMap(
                        relationshipToSubject -> relationshipToSubject,
                        relationshipToSubject -> {
                            List<XtdSubject> result = service.getTargetSubjects(relationshipToSubject);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdRelationshipToSubject", field = "scopeSubjects")
    public Map<XtdRelationshipToSubject, List<XtdSubject>> getScopeSubjects(List<XtdRelationshipToSubject> relationshipToSubjects) {
        return relationshipToSubjects.stream()
                .filter(relationshipToSubject -> relationshipToSubject != null)  // Filter out null relationships
                .collect(Collectors.toMap(
                        relationshipToSubject -> relationshipToSubject,
                        relationshipToSubject -> {
                            List<XtdSubject> result = service.getScopeSubjects(relationshipToSubject);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdRelationshipToSubject", field = "relationshipType")
    public Map<XtdRelationshipToSubject, XtdRelationshipType> getRelationshipType(List<XtdRelationshipToSubject> relationshipToSubjects) {
        Map<XtdRelationshipToSubject, XtdRelationshipType> result = new java.util.HashMap<>();
        for (XtdRelationshipToSubject relationshipToSubject : relationshipToSubjects) {
            if (relationshipToSubject != null) {
                XtdRelationshipType relationshipType = service.getRelationshipType(relationshipToSubject);
                if (relationshipType != null) {
                    result.put(relationshipToSubject, relationshipType);
                }
            }
        }
        return result;
    }

}
