package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.domain.collection.XtdNest;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.graphql.resolver.XtdCollectionTypeResolver;
import de.bentrm.datacat.service.XtdCollectionService;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLCodeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Optional;

import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLCodeRegistry.newCodeRegistry;

//@Configuration
public class XtdCollectionDataFetchers {

    @Autowired
    private XtdCollectionService collectionService;

//    @Bean("collectionCodeRegistry")
    public GraphQLCodeRegistry buildCodeRegistry() {
        return newCodeRegistry()
                .typeResolver(XtdCollection.LABEL, new XtdCollectionTypeResolver())
                .dataFetcher(coordinates("query", "bag"), collectionById())
                .dataFetcher(coordinates("query", "bags"), collectionsByMatch(XtdBag.LABEL))
                .dataFetcher(coordinates("query", "nest"), collectionById())
                .dataFetcher(coordinates("query", "nests"), collectionsByMatch(XtdNest.LABEL))
                .build();
    }

    public DataFetcher<Optional<XtdCollection>> collectionById() {
        return env -> {
            String id = env.getArgument("id");
            return collectionService.findById(id);
        };
    }

    public DataFetcher<Connection<XtdCollection>> collectionsByMatch(String label) {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();
            Page<XtdCollection> page = collectionService.findAll(label, dto.getPageNumber(), dto.getPageSize());
            return new Connection<>(page);
        };
    }

}
