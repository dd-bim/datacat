package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.RelGroupsService;
import de.bentrm.datacat.service.SubjectService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class SubjectDataFetcherProvider implements EntityDataFetcherProvider<XtdSubject> {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private RelGroupsService relGroupsService;

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("subject", getOne()),
                Map.entry("subjects", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createSubject", add()),
                Map.entry("updateSubject", update()),
                Map.entry("deleteSubject", remove())
        );
    }

    @Override
    public DataFetcher<XtdSubject> add() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            RootInput dto = mapper.convertValue(input, RootInput.class);
            return subjectService.create(dto);
        };
    }

    @Override
    public DataFetcher<XtdSubject> update() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            ObjectMapper mapper = new ObjectMapper();
            RootUpdateInput dto = mapper.convertValue(input, RootUpdateInput.class);
            return subjectService.update(dto);
        };
    }


    @Override
    public DataFetcher<Optional<XtdSubject>> remove() {
        return environment -> {
            String id = environment.getArgument("id");
            return subjectService.delete(id);
        };
    }

    @Override
    public DataFetcher<Optional<XtdSubject>> getOne() {
        return env -> {
            String id = env.getArgument("id");
            return subjectService.findById(id);
        };
    }

    @Override
    public DataFetcher<Connection<XtdSubject>> getAll() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("options");
            ObjectMapper mapper = new ObjectMapper();
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
            if (dto == null) dto = PagingOptions.defaults();

            Page<XtdSubject> page;
            String term = environment.getArgument("term");
            if (term != null && !term.isBlank()) {
                page = subjectService.findByTerm(term.trim(), dto.getPageble());
            } else {
                page = subjectService.findAll(dto.getPageble());
            }

            return new Connection<>(page);
        };
    }

//    public DataFetcher<XtdSubject> addComment() {
//        return environment -> {
//            String id = environment.getArgument("id");
//            Map<String, Object> input = environment.getArgument("comment");
//            ObjectMapper mapper = new ObjectMapper();
//            CommentInput newComment = mapper.convertValue(input, CommentInput.class);
//            return subjectService.addComment(id, newComment);
//        };
//    }
}
