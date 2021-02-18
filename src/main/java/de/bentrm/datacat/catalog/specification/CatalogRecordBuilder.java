package de.bentrm.datacat.catalog.specification;

import de.bentrm.datacat.base.specification.GenericBuilder;
import de.bentrm.datacat.base.specification.HasLabelComparison;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.domain.Translation;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ToString(callSuper = true)
public abstract class CatalogRecordBuilder<B extends CatalogRecordBuilder<B>> extends GenericBuilder<B> {

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

    public B entityTypeIn(final List<CatalogRecordType> recordTypes) {
        final List<String> labels = recordTypes.stream()
                .map(CatalogRecordType::getLabel)
                .collect(Collectors.toList());
        final Filter filter = new Filter(new HasLabelComparison(labels));
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);
        return self();
    }

    public B entityTypeNotIn(final List<CatalogRecordType> recordTypes) {
        final List<String> labels = recordTypes.stream()
                .map(CatalogRecordType::getLabel)
                .collect(Collectors.toList());
        final Filter filter = new Filter(new HasLabelComparison(labels));
        filter.setBooleanOperator(BooleanOperator.AND);
        filter.setNegated(true);
        filters.add(filter);
        return self();
    }

    public B tagged(final List<String> tagIds) {
        return related("id", tagIds, new Filter.NestedPathSegment("tags", Tag.class));
    }

}
