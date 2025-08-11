package de.bentrm.datacat.catalog.specification;

import de.bentrm.datacat.base.specification.QuerySpecification;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static de.bentrm.datacat.catalog.domain.XtdObject.DEFAULT_LANGUAGE_TAG;

@Slf4j
@ToString(callSuper = true)
public final class LanguageSpecification extends QuerySpecification {

    public static Builder builder() {
        return new Builder();
    }

    private LanguageSpecification(List<String> filters, Integer pageNumber, Integer pageSize) {
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
        public LanguageSpecification.Builder query(String query) {
            final Optional<String> regex = sanitizeQueryString(query);
            if (regex.isPresent()) {
                final String textFilter = " n.englishName =~ '" + regex.get() + "'" + " OR n.nativeName =~ '" + regex.get() + "'";
                this.filters.add(textFilter);
            }
            return self();
        }

        @Override
        public LanguageSpecification build() {
            return new LanguageSpecification(this.filters, this.pageNumber, this.pageSize);
        }

    }
}
