package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.*;
import de.bentrm.datacat.dto.SearchOptionsDto;
import de.bentrm.datacat.dto.XtdActivityInputDto;
import de.bentrm.datacat.dto.XtdActorInputDto;
import de.bentrm.datacat.dto.XtdSubjectInputDto;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.resolver.XtdObjectTypeResolver;
import de.bentrm.datacat.service.XtdObjectService;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLCodeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import static graphql.schema.FieldCoordinates.coordinates;
import static graphql.schema.GraphQLCodeRegistry.newCodeRegistry;

@Component
public class XtdObjectDataFetchers {

    public final XtdObjectService objectService;

    @Autowired
    public XtdObjectDataFetchers(XtdObjectService objectService) {
        this.objectService = objectService;
    }

    @Bean("objectCodeRegistry")
    public GraphQLCodeRegistry buildCodeRegistry() {
        return newCodeRegistry()
                .typeResolver(XtdObject.LABEL, new XtdObjectTypeResolver())
                .dataFetcher(coordinates("mutation", "addActor"), addActor())
                .dataFetcher(coordinates("mutation", "deleteActor"), deleteActor())
                .dataFetcher(coordinates("query", "actor"), objectById())
                .dataFetcher(coordinates("query", "actors"), objectsByMatch(XtdActor.LABEL))
                .dataFetcher(coordinates("mutation", "addActivity"), addActivity())
                .dataFetcher(coordinates("mutation", "deleteActivity"), deleteActivity())
                .dataFetcher(coordinates("query", "activity"), objectById())
                .dataFetcher(coordinates("query", "activities"), objectsByMatch(XtdActivity.LABEL))
                .dataFetcher(coordinates("query", "classification"), objectById())
                .dataFetcher(coordinates("query", "classifications"), objectsByMatch(XtdClassification.LABEL))
                .dataFetcher(coordinates("query", "measure"), objectById())
                .dataFetcher(coordinates("query", "measures"), objectsByMatch(XtdMeasureWithUnit.LABEL))
                .dataFetcher(coordinates("query", "property"), objectById())
                .dataFetcher(coordinates("query", "properties"), objectsByMatch(XtdProperty.LABEL))
                .dataFetcher(coordinates("mutation", "addSubject"), addSubject())
                .dataFetcher(coordinates("mutation", "deleteSubject"), deleteSubject())
                .dataFetcher(coordinates("query", "subject"), objectById())
                .dataFetcher(coordinates("query", "subjects"), subjectsByMatch())
                .dataFetcher(coordinates("query", "unit"), objectById())
                .dataFetcher(coordinates("query", "units"), objectsByMatch(XtdUnit.LABEL))
                .dataFetcher(coordinates("query", "value"), objectById())
                .dataFetcher(coordinates("query", "values"), objectsByMatch(XtdValue.LABEL))
                .build();
    }

    public DataFetcher<XtdActor> addActor() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("newActor");
            ObjectMapper mapper = new ObjectMapper();
            XtdActorInputDto dto = mapper.convertValue(input, XtdActorInputDto.class);
            return objectService.createActor(dto);
        };
    }

    public DataFetcher<XtdActor> deleteActor() {
        return environment -> {
            String id = environment.getArgument("id");
            return objectService.deleteActor(id);
        };
    }

    public DataFetcher<XtdActivity> addActivity() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("newActivity");
            ObjectMapper mapper = new ObjectMapper();
            XtdActivityInputDto dto = mapper.convertValue(input, XtdActivityInputDto.class);
            return objectService.createActivity(dto);
        };
    }

    public DataFetcher<XtdActivity> deleteActivity() {
        return environment -> {
            String id = environment.getArgument("id");
            return objectService.deleteActivity(id);
        };
    }

    public DataFetcher<XtdSubject> addSubject() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("newSubject");
            ObjectMapper mapper = new ObjectMapper();
            XtdSubjectInputDto dto = mapper.convertValue(input, XtdSubjectInputDto.class);
            return objectService.createSubject(dto);
        };
    }

    public DataFetcher<XtdSubject> deleteSubject() {
        return environment -> {
            String id = environment.getArgument("id");
            return objectService.deleteSubject(id);
        };
    }

    public DataFetcher<XtdObject> objectById() {
        return env -> {
            String id = env.getArgument("id");
            return objectService.findById(id);
        };
    }

    public DataFetcher<Connection<XtdObject>> objectsByMatch(String label) {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto searchOptionsDto = mapper.convertValue(input, SearchOptionsDto.class);

            if (searchOptionsDto == null) {
                searchOptionsDto = new SearchOptionsDto();
                searchOptionsDto.setPageNumber(0);
                searchOptionsDto.setPageSize(10);
            }

            Page<XtdObject> page = objectService.findAll(label, searchOptionsDto.getPageNumber(), searchOptionsDto.getPageSize());
            Connection<XtdObject> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));

            return connection;
        };
    }

    public DataFetcher<Connection<XtdSubject>> subjectsByMatch() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) {
                dto = new SearchOptionsDto();
                dto.setPageNumber(0);
                dto.setPageSize(10);
            }

            Page<XtdSubject> page;
            if (dto.getTerm() != null && !dto.getTerm().isBlank()) {
                page = objectService.findSubjectsByTerm(dto.getTerm(), dto.getPageNumber(), dto.getPageSize());
            } else {
                page = objectService.findAllSubjects(dto.getPageNumber(), dto.getPageSize());
            }

            Connection<XtdSubject> connection = new Connection<>();
            connection.setNodes(page.get().collect(Collectors.toList()));
            connection.setPage(PageInfo.fromPage(page));

            return connection;
        };
    }

}
