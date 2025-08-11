package de.bentrm.datacat.auth.specification;

import de.bentrm.datacat.base.specification.GenericBuilder;
import de.bentrm.datacat.base.specification.QuerySpecification;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Slf4j
@ToString
public class UserSpecification extends QuerySpecification {

    public static Builder builder() {
        return new Builder();
    }

    protected UserSpecification(List<String> filters, Integer pageNumber, Integer pageSize) {
        super(filters, Sort.Direction.ASC, List.of("username"), pageNumber, pageSize);
    }

    @Slf4j
    @ToString
    public static final class Builder extends GenericBuilder<Builder> {

        protected Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Builder query(String query) {
            final Optional<String> regex = sanitizeQueryString(query);
            if (regex.isPresent()) {
                final String filter = "n.username =~ '.*" + regex.get() + ".*'";
                this.filters.add(filter);
            }
            return self();
        }

        public Builder expired(final Boolean isExpired) {
            final String filter = "n.expired = " + isExpired;
            filters.add(filter);
            return this;
        }

        public Builder locked(final Boolean isLocked) {
            final String filter = "n.locked = " + isLocked;
            filters.add(filter);
            return this;
        }

        public Builder credentialsExpired(final Boolean isCredentialsExpired) {
            final String filter = "n.credentialsExpired = " + isCredentialsExpired;
            filters.add(filter);
            return this;
        }

        public UserSpecification build() {
            return new UserSpecification(this.filters, this.pageNumber, this.pageSize);
        }
    }
}
