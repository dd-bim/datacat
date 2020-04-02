package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdActor;
import de.bentrm.datacat.service.ActorService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ActorDataFetcherProvider
		extends EntityDataFetcherProviderImpl<XtdActor, ActorService>
		implements EntityDataFetcherProvider<XtdActor> {

	public ActorDataFetcherProvider(ActorService entityService) {
		super(entityService);
	}

	@Override
	public Map<String, DataFetcher> getQueryDataFetchers() {
		return Map.ofEntries(
				Map.entry("actor", getOne()),
				Map.entry("actors", getAll())
		);
	}

	@Override
	public Map<String, DataFetcher> getMutationDataFetchers() {
		return Map.ofEntries(
				Map.entry("createActor", add()),
				Map.entry("updateActor", update()),
				Map.entry("deleteActor", remove())
		);
	}
}
