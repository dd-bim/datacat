package de.bentrm.datacat.specification;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.Filters;
import org.springframework.data.domain.Sort;

import java.util.List;

import static de.bentrm.datacat.domain.CatalogItem.DEFAULT_LANGUAGE_TAG;

@Slf4j
@ToString(callSuper = true)
public final class CatalogItemSpecification extends QuerySpecification {

    public static Builder builder() {
        return new Builder();
    }

    private CatalogItemSpecification(Filters filters, Integer pageNumber, Integer pageSize) {
        super(filters, Sort.Direction.ASC, List.of("labels." + DEFAULT_LANGUAGE_TAG), pageNumber, pageSize);
    }

    @Slf4j
    @ToString(callSuper = true)
    public static final class Builder extends CatalogItemBuilder<Builder> {

        private Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        public CatalogItemSpecification build() {
            return new CatalogItemSpecification(this.filters, this.pageNumber, this.pageSize);
        }
    }
}
