package de.bentrm.datacat.catalog.specification;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.*;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.springframework.data.domain.Sort;

import java.util.List;

import static de.bentrm.datacat.catalog.domain.CatalogRecord.DEFAULT_LANGUAGE_TAG;

@Slf4j
@ToString(callSuper = true)
public final class RootSpecification extends QuerySpecification {

    public static Builder builder() {
        return new Builder();
    }

    protected RootSpecification(Filters filters, Integer pageNumber, Integer pageSize) {
        super(filters, Sort.Direction.ASC, List.of("labels." + DEFAULT_LANGUAGE_TAG), pageNumber, pageSize);
    }

    @Slf4j
    @ToString(callSuper = true)
    public static final class Builder extends CatalogRecordBuilder<Builder> {

        @Override
        protected Builder self() {
            return this;
        }

        public Builder collectedBy(String id) {
            return related("id", id, new Filter.NestedPathSegment("collectedBy", XtdRelCollects.class));
        }

        // public Builder documentedBy(String id) {
        //     return related("id", id, new Filter.NestedPathSegment("documentedBy", XtdRelDocuments.class));
        // }

        @Override
        public RootSpecification build() {
            return new RootSpecification(this.filters, this.pageNumber, this.pageSize);
        }
    }
}
