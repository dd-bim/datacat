package de.bentrm.datacat.catalog.specification;

import de.bentrm.datacat.base.specification.GenericBuilder;
import de.bentrm.datacat.base.specification.QuerySpecification;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static de.bentrm.datacat.catalog.domain.XtdObject.DEFAULT_LANGUAGE_TAG;

@Slf4j
@ToString(callSuper = true)
public class TagSpecification extends QuerySpecification {

    public static TagSpecification.Builder builder() {
        return new TagSpecification.Builder();
    }

    private TagSpecification(List<String> filters, Integer pageNumber, Integer pageSize) {
        super(filters, Sort.Direction.ASC, List.of("labels." + DEFAULT_LANGUAGE_TAG), pageNumber, pageSize);
    }

    @Slf4j
    @ToString(callSuper = true)
    public static final class Builder extends GenericBuilder<Builder> {

        private Builder() {
        }

        @Override
        protected TagSpecification.Builder self() {
            return this;
        }

        @Override
        public TagSpecification.Builder query(String query) {
            final Optional<String> regex = sanitizeQueryString(query);
            if (regex.isPresent()) {
                final String textFilter = "EXISTS {MATCH(n)-[:NAMES]->(y:XtdMultiLanguageText)-[:TEXTS]->(z:XtdText) WHERE z.text =~ '.*" + regex.get() + ".*'}";
                this.filters.add(textFilter);
            }
            return self();
        }

        @Override
        public TagSpecification build() {
            return new TagSpecification(this.filters, this.pageNumber, this.pageSize);
        }

    }
}
