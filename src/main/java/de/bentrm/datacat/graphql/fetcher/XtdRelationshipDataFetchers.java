package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.graphql.dto.XtdRelGroupsInput;
import de.bentrm.datacat.graphql.resolver.XtdRelationshipTypeResolver;
import de.bentrm.datacat.service.XtdObjectService;
import de.bentrm.datacat.service.XtdRelationshipService;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLCodeRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;

import java.util.Map;

import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLCodeRegistry.newCodeRegistry;

//@Configuration
public class XtdRelationshipDataFetchers {

//    @Autowired
    private XtdObjectService objectService;

//    @Autowired
    private XtdRelationshipService relationshipService;

    @Bean("relationshipCodeRegistry")
    public GraphQLCodeRegistry buildCodeRegistry() {
        return newCodeRegistry()
                .typeResolver(XtdRelationship.LABEL, new XtdRelationshipTypeResolver())

                .dataFetcher(coordinates("query", "documentsRelationships"), relDocumentsById())
                .dataFetcher(coordinates("query", "documentsRelationships"), relDocuments())
                .dataFetcher(coordinates(XtdExternalDocument.LABEL, "documents"), relDocumentsByRelatingDocument())
                .dataFetcher(coordinates(XtdRelDocuments.LABEL, "relatedThings"), relDocumentsRelatedThings())

                .dataFetcher(coordinates("mutation", "addGroupsRelationship"), addRelGroups())
                .dataFetcher(coordinates("query", "groupsRelationship"), relGroupsById())
                .dataFetcher(coordinates("query", "groupsRelationships"), relGroups())
                .dataFetcher(coordinates(XtdSubject.LABEL, "groups"), relGroupsByRelatingObject())
                .dataFetcher(coordinates(XtdRelGroups.LABEL, "relatedObjects"), relGroupsRelatedObjects())
                .build();
    }


    public DataFetcher<XtdRelDocuments> relDocumentsById() {
        return env -> {
            String id = env.getArgument("id");
            return relationshipService.findRelDocumentsById(id);
        };
    }

    private DataFetcher<Connection<XtdRelDocuments>> relDocuments() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelDocuments> page = relationshipService.findAllRelDocuments(dto.getPageNumber(), dto.getPageSize());


            return new Connection<>(page);
        };
    }

    private DataFetcher<Connection<XtdObject>> relDocumentsRelatedThings() {
        return environment -> {
            XtdRelDocuments relationship = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdObject> page = objectService.findByRelDocumentsId(relationship.getId(), dto.getPageNumber(), dto.getPageSize());

            return new Connection<>(page);
        };
    }

    private DataFetcher<Connection<XtdRelDocuments>> relDocumentsByRelatingDocument() {
        return environment -> {
            XtdExternalDocument document = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelDocuments> page = relationshipService.findRelDocumentsByRelatingDocument(document.getId(), dto.getPageNumber(), dto.getPageSize());

            return new Connection<>(page);
        };
    }

    private DataFetcher<XtdRelGroups> addRelGroups() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            XtdRelGroupsInput dto = mapper.convertValue(input, XtdRelGroupsInput.class);
            return relationshipService.createRelGroups(dto);
        };
    }

    public DataFetcher<XtdRelGroups> relGroupsById() {
        return environment -> {
            String id = environment.getArgument("id");
            return relationshipService.findRelGroupsById(id);
        };
    }

    public DataFetcher<Connection<XtdRelGroups>> relGroups() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = new PagingOptions();

            Page<XtdRelGroups> page;
//            if (dto.hasRelatingObject()) {
//                page = relationshipService.findRelGroupsByRelatingObjectId(dto.getRelatingObject(), dto.getPageNumber(), dto.getPageSize());
//            } else {
                page = relationshipService.findAllRelGroups(dto.getPageNumber(), dto.getPageSize());
//            }

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelGroups>> relGroupsByRelatingObject() {
        return environment -> {
            XtdObject parent = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdRelGroups> page = relationshipService.findRelGroupsByRelatingObjectId(parent.getId(), dto.getPageNumber(), dto.getPageSize());

            return new Connection<>(page);
        };
    }

    private DataFetcher<Connection<XtdObject>> relGroupsRelatedObjects() {
        return environment -> {
            XtdRelGroups relationship = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdObject> page = objectService.findByRelGroupsId(relationship.getId(), dto.getPageNumber(), dto.getPageSize());

            return new Connection<>(page);
        };
    }

}
