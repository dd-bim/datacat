package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdRoot;
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

import java.util.ArrayList;
import java.util.List;
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

                .dataFetcher(coordinates("query", "documentsRelationships"), relDocumentsByUniqueId())
                .dataFetcher(coordinates("query", "documentsRelationships"), relDocuments())
                .dataFetcher(coordinates(XtdExternalDocument.LABEL, "documents"), relDocumentsByRelatingDocument())
                .dataFetcher(coordinates(XtdRelDocuments.LABEL, "relatedThings"), relDocumentsRelatedThings())

                .dataFetcher(coordinates("mutation", "addGroupsRelationship"), addRelGroups())
                .dataFetcher(coordinates("query", "groupsRelationship"), relGroupsByUniqueId())
                .dataFetcher(coordinates("query", "groupsRelationships"), relGroups())
                .dataFetcher(coordinates(XtdSubject.LABEL, "groups"), relGroupsByRelatingObject())
                .dataFetcher(coordinates(XtdRelGroups.LABEL, "relatedObjects"), relGroupsRelatedObjects())
                .build();
    }


    public DataFetcher<XtdRelDocuments> relDocumentsByUniqueId() {
        return env -> {
            String uniqueId = env.getArgument("uniqueId");
            return relationshipService.findRelDocumentsByUniqueId(uniqueId);
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

            Page<XtdObject> page = objectService.findByRelDocumentsUniqueId(relationship.getUniqueId(), dto.getPageNumber(), dto.getPageSize());

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

            Page<XtdRelDocuments> page = relationshipService.findRelDocumentsByRelatingDocument(document.getUniqueId(), dto.getPageNumber(), dto.getPageSize());

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

    public DataFetcher<XtdRelGroups> relGroupsByUniqueId() {
        return environment -> {
            String uniqueId = environment.getArgument("uniqueId");
            return relationshipService.findRelGroupsByUniqueId(uniqueId);
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
                page = relationshipService.findRelGroupsByRelatingObjectUniqueId(dto.getRelatingObject(), dto.getPageNumber(), dto.getPageSize());
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

            System.out.println(parent.getUniqueId());

            Page<XtdRelGroups> page = relationshipService.findRelGroupsByRelatingObjectUniqueId(parent.getUniqueId(), dto.getPageNumber(), dto.getPageSize());

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

            Page<XtdObject> page = objectService.findByRelGroupsUniqueId(relationship.getUniqueId(), dto.getPageNumber(), dto.getPageSize());

            Connection<XtdObject> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
        };
    }

}
