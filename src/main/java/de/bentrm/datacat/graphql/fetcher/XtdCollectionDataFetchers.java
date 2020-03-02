package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.domain.collection.XtdNest;
import de.bentrm.datacat.dto.SearchOptionsDto;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.resolver.XtdCollectionTypeResolver;
import de.bentrm.datacat.service.XtdCollectionService;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLCodeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLCodeRegistry.newCodeRegistry;

@Configuration
public class XtdCollectionDataFetchers {

    public XtdCollectionService collectionService;

    @Autowired
    public XtdCollectionDataFetchers(XtdCollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Bean("collectionCodeRegistry")
    public GraphQLCodeRegistry buildCodeRegistry() {
        return newCodeRegistry()
                .typeResolver(XtdCollection.LABEL, new XtdCollectionTypeResolver())
                .dataFetcher(coordinates("query", "bag"), collectionById())
                .dataFetcher(coordinates("query", "bags"), collectionsByMatch(XtdBag.LABEL))
                .dataFetcher(coordinates("query", "nest"), collectionById())
                .dataFetcher(coordinates("query", "nests"), collectionsByMatch(XtdNest.LABEL))
                .build();
    }

    public DataFetcher<XtdCollection> collectionById() {
        return env -> {
            String id = env.getArgument("id");
            return collectionService.findById(id);
        };
    }

    public DataFetcher<Connection<XtdCollection>> collectionsByMatch(String label) {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) {
                dto = new SearchOptionsDto();
                dto.setPageNumber(0);
                dto.setPageSize(10);
            }

            Page<XtdCollection> page = collectionService.findAll(label, dto.getPageNumber(), dto.getPageSize());
            Connection<XtdCollection> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));
            return connection;
        };
    }

}
