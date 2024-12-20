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
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class RelationshipToSubjectController {

    @Autowired
    private RelationshipToSubjectRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdRelationshipToSubject> getRelationshipToSubject(@Argument String id) {
        return service.findByIdWithDirectRelations(id);
    }

    @QueryMapping
    public Connection<XtdRelationshipToSubject> findRelationshipToSubjects(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdRelationshipToSubject> page = service.findAll(specification);
        return Connection.of(page);
    }

    @SchemaMapping(typeName = "XtdRelationshipToSubject", field = "connectingSubject")
    public XtdSubject getConnectingSubject(XtdRelationshipToSubject relationshipToSubject) {
        return service.getConnectingSubject(relationshipToSubject);
    }

    @SchemaMapping(typeName = "XtdRelationshipToSubject", field = "targetSubjects")
    public List<XtdSubject> getTargetSubjects(XtdRelationshipToSubject relationshipToSubject) {
        return service.getTargetSubjects(relationshipToSubject);
    }

    @SchemaMapping(typeName = "XtdRelationshipToSubject", field = "scopeSubjects")
    public List<XtdSubject> getScopeSubjects(XtdRelationshipToSubject relationshipToSubject) {
        return service.getScopeSubjects(relationshipToSubject);
    }

    @SchemaMapping(typeName = "XtdRelationshipToSubject", field = "relationshipType")
    public XtdRelationshipType getRelationshipType(XtdRelationshipToSubject relationshipToSubject) {
        return service.getRelationshipType(relationshipToSubject);
    }

}
