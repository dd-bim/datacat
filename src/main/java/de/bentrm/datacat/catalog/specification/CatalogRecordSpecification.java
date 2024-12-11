package de.bentrm.datacat.catalog.specification;

import de.bentrm.datacat.base.specification.QuerySpecification;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;

import java.util.List;

import static de.bentrm.datacat.catalog.domain.XtdObject.DEFAULT_LANGUAGE_TAG;

@Slf4j
@ToString(callSuper = true)
public final class CatalogRecordSpecification extends QuerySpecification {

    public static Builder builder() {
        return new Builder();
    }

    private CatalogRecordSpecification(List<String> filters, Integer pageNumber, Integer pageSize) {
        super(filters, Sort.Direction.ASC, List.of("labels." + DEFAULT_LANGUAGE_TAG), pageNumber, pageSize);
    }

    @Slf4j
    @ToString(callSuper = true)
    public static final class Builder extends CatalogRecordBuilder<Builder> {

        private Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public CatalogRecordSpecification build() {
            return new CatalogRecordSpecification(this.filters, this.pageNumber, this.pageSize);
        }

    }
}
