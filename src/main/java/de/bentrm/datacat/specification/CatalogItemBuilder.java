package de.bentrm.datacat.specification;

import de.bentrm.datacat.domain.Translation;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;

import java.util.Optional;

@Slf4j
@ToString(callSuper = true)
public abstract class CatalogItemBuilder<B extends CatalogItemBuilder<B>> extends GenericBuilder<B> {

    @Override
    public B query(String query) {
        final Optional<String> regex = sanitizeQueryString(query);
        if (regex.isPresent()) {
            final Filter filter = new Filter("label", ComparisonOperator.LIKE, regex.get());
            filter.setBooleanOperator(BooleanOperator.AND);
            filter.setNestedPath(new Filter.NestedPathSegment("names", Translation.class));
            this.filters.add(filter);
        }
        return self();
    }
}
