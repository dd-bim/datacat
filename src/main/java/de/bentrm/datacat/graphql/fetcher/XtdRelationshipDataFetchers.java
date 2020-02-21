package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import de.bentrm.datacat.dto.SearchOptionsDto;
import de.bentrm.datacat.dto.XtdRelGroupsInputDto;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.resolver.XtdRelationshipTypeResolver;
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
    private XtdRelationshipService relationshipService;

    @Bean("relationshipCodeRegistry")
    public GraphQLCodeRegistry buildCodeRegistry() {
        return newCodeRegistry()
                .typeResolver(XtdRelationship.LABEL, new XtdRelationshipTypeResolver())
                .dataFetcher(coordinates("mutation", "addGroupsRelationship"), addRelGroups())
                .dataFetcher(coordinates("query", "getRelGroups"), relGroupsByUniqueId())
                .dataFetcher(coordinates("query", "findRelGroups"), relGroups())
                .dataFetcher(coordinates(XtdSubject.LABEL, "associates"), relGroupsByRelatingObjectUniqueId())
                .build();
    }

    private DataFetcher<XtdRelGroups> addRelGroups() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            XtdRelGroupsInputDto dto = mapper.convertValue(input, XtdRelGroupsInputDto.class);
            return relationshipService.createRelGroups(dto);
        };
    }

    public DataFetcher<XtdRelationship> relationshipByUniqueId() {
        return env -> {
            String uniqueId = env.getArgument("uniqueId");
            return relationshipService.findByUniqueId(uniqueId);
        };
    }

    public DataFetcher<Connection<XtdRelationship>> relationshipsByMatch(String label) {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto searchOptionsDto = mapper.convertValue(input, SearchOptionsDto.class);

            if (searchOptionsDto == null) {
                searchOptionsDto = new SearchOptionsDto();
                searchOptionsDto.setPageNumber(0);
                searchOptionsDto.setPageSize(10);
            }

            Page<XtdRelationship> page = relationshipService.findAll(label, searchOptionsDto.getPageNumber(), searchOptionsDto.getPageSize());
            Connection<XtdRelationship> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
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
            Iterable<XtdRelGroups> nodes = relationshipService.findRelGroups();
            List<XtdRelGroups> results = new ArrayList<>();
            nodes.forEach(results::add);
            Connection<XtdRelGroups> connection = new Connection<>();
            connection.setNodes(results);

            results.forEach(System.out::println);

            return connection;
        };
    }

    public DataFetcher<List<XtdRelGroups>> relGroupsByRelatingObjectUniqueId() {
        return environment -> {
            XtdRoot root = environment.getSource();
            String uniqueId = root.getUniqueId();
            return relationshipService.findAsscociationsByRelatingObjectUniqueId(uniqueId);
        };
    }

}
