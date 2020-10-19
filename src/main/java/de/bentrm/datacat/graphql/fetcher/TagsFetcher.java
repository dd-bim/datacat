package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import de.bentrm.datacat.service.dto.TagDto;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.stream.Collectors;

public class TagsFetcher implements DataFetcher<List<TagDto>> {

    private final ValueMapper valueMapper = ValueMapper.INSTANCE;

    @Override
    public List<TagDto> get(DataFetchingEnvironment environment) throws Exception {
        final XtdRoot source = environment.getSource();
        return source.getTags().stream()
                .map(valueMapper::toDto)
                .collect(Collectors.toList());
    }

}
