package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.BagService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BagFetchers extends AbstractEntityFetchers<XtdBag, RootInput, RootUpdateInput, BagService> {

    public BagFetchers(BagService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdBag";
    }

    @Override
    public String getQueryName() {
        return "bags";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Bag";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("collects", collects());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRelCollects>> collects() {
        return env -> {
            XtdBag source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .entityTypeIn(List.of("XtdRelCollects"))
                    .idIn(source.getCollects().stream().map(Entity::getId).collect(Collectors.toList()))
                    .build();

            // only the size of the connection is accessed
            if (!env.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
                // property might have been fetched and populated before so we can count the sets size
                if (source.getCollects() != null) {
                    return Connection.empty(source.getCollects().size());
                }
                final @NotNull long count = catalogService.countRootItems(specification);
                return Connection.empty(count);
            }

            // the property needs to be populated
            @NotNull final Page<XtdRoot> page = catalogService.findAllRootItems(specification);
            final List<XtdRelCollects> content = page.get().map(x -> (XtdRelCollects) x).collect(Collectors.toList());
            return new Connection<>(content, PageInfo.of(page), page.getTotalElements());
        };
    }
}
