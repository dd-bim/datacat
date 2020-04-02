package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.RootService;
import graphql.schema.DataFetcher;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Optional;

public abstract class EntityDataFetcherProviderImpl<T extends XtdRoot, S extends RootService<T>>
		implements EntityDataFetcherProvider<T> {

	private S entityService;

	public EntityDataFetcherProviderImpl(S entityService) {
		this.entityService = entityService;
	}

	@Override
	public DataFetcher<T> add() {
		return environment -> {
			Map<String, Object> input = environment.getArgument("input");
			ObjectMapper mapper = new ObjectMapper();
			RootInput dto = mapper.convertValue(input, RootInput.class);
			return entityService.create(dto);
		};
	}

	@Override
	public DataFetcher<T> update() {
		return environment -> {
			Map<String, Object> input = environment.getArgument("input");
			ObjectMapper mapper = new ObjectMapper();
			RootUpdateInput dto = mapper.convertValue(input, RootUpdateInput.class);
			return entityService.update(dto);
		};
	}


	@Override
	public DataFetcher<Optional<T>> remove() {
		return environment -> {
			String id = environment.getArgument("id");
			return entityService.delete(id);
		};
	}

	@Override
	public DataFetcher<Optional<T>> getOne() {
		return env -> {
			String id = env.getArgument("id");
			return entityService.findById(id);
		};
	}

	@Override
	public DataFetcher<Connection<T>> getAll() {
		return environment -> {
			Map<String, Object> input = environment.getArgument("options");
			ObjectMapper mapper = new ObjectMapper();
			PagingOptions dto = mapper.convertValue(input, PagingOptions.class);
			if (dto == null) dto = PagingOptions.defaults();

			Page<T> page;
			String term = environment.getArgument("term");
			if (term != null && !term.isBlank()) {
				page = entityService.findByTerm(term.trim(), dto.getPageble());
			} else {
				page = entityService.findAll(dto.getPageble());
			}

			return new Connection<>(page);
		};
	}

}
