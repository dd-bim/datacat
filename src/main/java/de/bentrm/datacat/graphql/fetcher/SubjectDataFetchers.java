package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.dto.PagingOptionsDto;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.service.RelGroupsService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SubjectDataFetchers {

    @Autowired
    private RelGroupsService relGroupsService;

    public DataFetcher<Connection<XtdRelGroups>> groups() {
        return environment -> {
            XtdSubject subject = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptionsDto dto = mapper.convertValue(input, PagingOptionsDto.class);

            if (dto == null) dto = PagingOptionsDto.defaults();

            Page<XtdRelGroups> page = relGroupsService.findByRelatingObjectId(subject.getId(), dto.getPageble());

            return new Connection<>(page);
        };
    }

    public DataFetcher<Connection<XtdRelGroups>> groupedBy() {
        return environment -> {
            XtdSubject subject = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptionsDto dto = mapper.convertValue(input, PagingOptionsDto.class);

            if (dto == null) dto = PagingOptionsDto.defaults();

            Page<XtdRelGroups> page = relGroupsService.findByRelatedObjectId(subject.getId(), dto.getPageble());

            return new Connection<>(page);
        };
    }
}
