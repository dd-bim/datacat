package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
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
public class SubjectController {

    @Autowired
    private SubjectRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdSubject> getSubject(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdSubject.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdSubject> findSubjects(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdSubject> page = service.findAll(specification);
        return Connection.of(page);
    }

    @SchemaMapping(typeName = "XtdSubject", field = "properties")
    public List<XtdProperty> getProperties(XtdSubject subject) {
        return service.getProperties(subject);
    }

    @SchemaMapping(typeName = "XtdSubject", field = "connectedSubjects")
    public List<XtdRelationshipToSubject> getConnectedSubjects(XtdSubject subject) {
        return service.getConnectedSubjects(subject);
    }

    @SchemaMapping(typeName = "XtdSubject", field = "connectingSubjects")
    public List<XtdRelationshipToSubject> getConnectingSubjects(XtdSubject subject) {
        return service.getConnectingSubjects(subject);
    }
}
