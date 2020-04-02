package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.service.SubjectService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SubjectDataFetcherProvider
		extends EntityDataFetcherProviderImpl<XtdSubject, SubjectService>
		implements EntityDataFetcherProvider<XtdSubject> {

	public SubjectDataFetcherProvider(SubjectService entityService) {
		super(entityService);
	}

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
}
