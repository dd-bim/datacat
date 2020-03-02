package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import de.bentrm.datacat.dto.SearchOptionsDto;
import de.bentrm.datacat.dto.XtdRelGroupsInputDto;
import de.bentrm.datacat.dto.XtdRelGroupsSearchOptionsDto;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.resolver.XtdRelationshipTypeResolver;
import de.bentrm.datacat.service.XtdObjectService;
import de.bentrm.datacat.service.XtdRelationshipService;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLCodeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.stream.Collectors;

import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLCodeRegistry.newCodeRegistry;

@Configuration
public class XtdRelationshipDataFetchers {

    @Autowired
    private XtdObjectService objectService;

    @Autowired
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
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) dto = SearchOptionsDto.defaults();

            Page<XtdRelDocuments> page = relationshipService.findAllRelDocuments(dto.getPageNumber(), dto.getPageSize());


            Connection<XtdRelDocuments> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
        };
    }

    private DataFetcher<Connection<XtdObject>> relDocumentsRelatedThings() {
        return environment -> {
            XtdRelDocuments relationship = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) dto = SearchOptionsDto.defaults();

            Page<XtdObject> page = objectService.findByRelDocumentsId(relationship.getId(), dto.getPageNumber(), dto.getPageSize());

            Connection<XtdObject> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
        };
    }

    private DataFetcher<Connection<XtdRelDocuments>> relDocumentsByRelatingDocument() {
        return environment -> {
            XtdExternalDocument document = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) dto = SearchOptionsDto.defaults();

            Page<XtdRelDocuments> page = relationshipService.findRelDocumentsByRelatingDocument(document.getId(), dto.getPageNumber(), dto.getPageSize());

            Connection<XtdRelDocuments> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
        };
    }

    private DataFetcher<XtdRelGroups> addRelGroups() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            XtdRelGroupsInputDto dto = mapper.convertValue(input, XtdRelGroupsInputDto.class);
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
            XtdRelGroupsSearchOptionsDto dto = mapper.convertValue(input, XtdRelGroupsSearchOptionsDto.class);

            if (dto == null) dto = new XtdRelGroupsSearchOptionsDto();

            Page<XtdRelGroups> page;
            if (dto.hasRelatingObject()) {
                page = relationshipService.findRelGroupsByRelatingObjectId(dto.getRelatingObject(), dto.getPageNumber(), dto.getPageSize());
            } else {
                page = relationshipService.findAllRelGroups(dto.getPageNumber(), dto.getPageSize());
            }

            Connection<XtdRelGroups> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
        };
    }

    public DataFetcher<Connection<XtdRelGroups>> relGroupsByRelatingObject() {
        return environment -> {
            XtdObject parent = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) dto = SearchOptionsDto.defaults();

            System.out.println(parent.getId());

            Page<XtdRelGroups> page = relationshipService.findRelGroupsByRelatingObjectId(parent.getId(), dto.getPageNumber(), dto.getPageSize());

            Connection<XtdRelGroups> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
        };
    }

    private DataFetcher<Connection<XtdObject>> relGroupsRelatedObjects() {
        return environment -> {
            XtdRelGroups relationship = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) dto = SearchOptionsDto.defaults();

            Page<XtdObject> page = objectService.findByRelGroupsId(relationship.getId(), dto.getPageNumber(), dto.getPageSize());

            Connection<XtdObject> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
        };
    }

}
