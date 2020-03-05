package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.dto.PagingOptionsDto;
import de.bentrm.datacat.dto.RootInputDto;
import de.bentrm.datacat.dto.SearchOptionsDto;
import de.bentrm.datacat.dto.XtdRelGroupsSearchOptionsDto;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.service.ObjectService;
import de.bentrm.datacat.service.RelGroupsService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RelGroupsDataFetchers {

    @Autowired
    private ObjectService objectService;

    @Autowired
    private RelGroupsService relGroupsService;

    public DataFetcher<XtdRelGroups> create() {
        return environment -> {
            String relatingObjectId = environment.getArgument("relatingObject");
            List<String> relatedObjectsIds = environment.getArgument("relatedObjects");
            Map<String, Object> input = environment.getArgument("relation");
            ObjectMapper mapper = new ObjectMapper();
            RootInputDto dto = mapper.convertValue(input, RootInputDto.class);
            return relGroupsService.create(relatingObjectId, new HashSet<>(relatedObjectsIds), dto);
        };
    }

    public DataFetcher<XtdRelGroups> addRelatedObjects() {
        return environment -> {
            String id = environment.getArgument("id");
            List<String> relatedObjectsIds = environment.getArgument("relatedObjects");
            return relGroupsService.addRelatedObjects(id, relatedObjectsIds);
        };
    }

    public DataFetcher<XtdRelGroups> removeRelatedObjects() {
        return environment -> {
            String id = environment.getArgument("id");
            List<String> relatedObjectsIds = environment.getArgument("relatedObjects");
            return relGroupsService.removeRelatedObjects(id, relatedObjectsIds);
        };
    }

    public DataFetcher<Optional<XtdRelGroups>> delete() {
        return environment -> {
            String id = environment.getArgument("id");
            return relGroupsService.delete(id);
        };
    }

    public DataFetcher<Optional<XtdRelGroups>> byId() {
        return environment -> {
            String id = environment.getArgument("id");
            return relGroupsService.findById(id);
        };
    }

    public DataFetcher<Connection<XtdRelGroups>> bySearchOptions() {
        return environment -> {
            String relatingObjectId = environment.getArgument("relatingObject");
            String relatedObjectId = environment.getArgument("relatedObject");
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            SearchOptionsDto dto = mapper.convertValue(input, SearchOptionsDto.class);

            if (dto == null) dto = new XtdRelGroupsSearchOptionsDto();

            Page<XtdRelGroups> page;
            if (relatingObjectId != null) {
                page = relGroupsService.findByRelatingObjectId(relatingObjectId, dto.getPageble());
            } else if (relatedObjectId != null) {
                page = relGroupsService.findByRelatedObjectId(relatedObjectId, dto.getPageble());
            } else {
                page = relGroupsService.findAll(dto.getPageble());
            }

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdObject>> relatedObjects() {
        return environment -> {
            XtdRelGroups group = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptionsDto dto = mapper.convertValue(input, PagingOptionsDto.class);

            if (dto == null) dto = PagingOptionsDto.defaults();

            Page<XtdObject> page = objectService.findByRelGroupsId(group.getId(), dto.getPageble());

            return new Connection<>(page);
        };
    }

}
