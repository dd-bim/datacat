package de.bentrm.datacat.catalog.specification;

import de.bentrm.datacat.base.specification.GenericBuilder;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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
        String textFilter = "EXISTS {MATCH(n)-[:NAMES]->(y:XtdMultiLanguageText)-[:TEXTS]->(z:XtdText) WHERE z.text =~ '.*" + regex.get() + ".*'}";

        // Add the filter to the list of filters
        this.filters.add(textFilter);
        }
        return self();
    }

    public B entityTypeIn(final List<CatalogRecordType> recordTypes) {
        final List<String> labels = recordTypes.stream()
                .map(CatalogRecordType::getLabel)
                .collect(Collectors.toList());
        final String filter = labels.stream()
                .map(label -> "n:" + label)
                .collect(Collectors.joining(" OR "));
        filters.add(filter);
        return self();
    }

    public B entityTypeNotIn(final List<CatalogRecordType> recordTypes) {
        final List<String> labels = recordTypes.stream()
                .map(CatalogRecordType::getLabel)
                .collect(Collectors.toList());
        final String filter = "NOT " + labels.stream()
                .map(label -> "n:" + label)
                .collect(Collectors.joining(" OR "));
        filters.add(filter);
        return self();
    }

    public B tagged(final List<String> tagIds) {
        String filter = "EXISTS { MATCH (n)-[:TAGGED]->(t:Tag) WHERE t.id IN ['" + String.join("', '", tagIds) + "'] }";
        filters.add(filter);
        return self();
    }

}
