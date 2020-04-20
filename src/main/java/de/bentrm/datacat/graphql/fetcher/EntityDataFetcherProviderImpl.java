package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.query.FilterOptions;
import de.bentrm.datacat.service.CrudEntityService;
import graphql.schema.DataFetcher;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class EntityDataFetcherProviderImpl<
			T extends XtdRoot, C, U,
			S extends CrudEntityService<T, C, U>
		>
		implements EntityDataFetcherProvider<T> {

	private final Class<C> inputClass;
	private final Class<U> updateClass;
	private final S entityService;

	public EntityDataFetcherProviderImpl(Class<C> inputClass, Class<U> updateClass, S entityService) {
		this.inputClass = inputClass;
		this.updateClass = updateClass;
		this.entityService = entityService;
	}

	@Override
	public DataFetcher<T> add() {
		return environment -> {
			Map<String, Object> input = environment.getArgument("input");
			ObjectMapper mapper = new ObjectMapper();
			C dto = mapper.convertValue(input, inputClass);
			return entityService.create(dto);
		};
	}

	@Override
	public DataFetcher<T> update() {
		return environment -> {
			Map<String, Object> input = environment.getArgument("input");
			ObjectMapper mapper = new ObjectMapper();
			U dto = mapper.convertValue(input, updateClass);
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
			List<String> excludedIds = environment.getArgument("excludedIds");
			if (term != null && !term.isBlank()) {
				page = entityService.findByTerm(term.trim(), dto.getPageble());
			} else {
				page = entityService.findAll(new FilterOptions<>(null, null, excludedIds), dto.getPageble());
			}

			return new Connection<>(page);
		};
	}

}
