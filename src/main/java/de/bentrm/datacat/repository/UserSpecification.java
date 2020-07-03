package de.bentrm.datacat.repository;

import lombok.ToString;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.util.Optional;

@ToString
public class UserSpecification {

    private final Filters filters;
    private final Integer pageNumber;
    private final Integer pageSize;

    public static UserSpecificationBuilder builder() {
        return new UserSpecificationBuilder();
    }

    private UserSpecification(final Filters filters, final Integer pageNumber, final Integer pageSize) {
        this.filters = filters != null ? filters : new Filters();
        this.pageNumber = pageNumber != null ? pageNumber : 0;
        this.pageSize = pageSize;
    }

    public Filters getFilters() {
        return filters;
    }

    public Optional<Pageable> getPageable() {
        if (pageSize != null) {
            return Optional.of(PageRequest.of(pageNumber, pageSize));
        }
        return Optional.empty();
    }

    @ToString
    public static class UserSpecificationBuilder {

        private Filters filters = new Filters();
        private Integer pageNumber;
        private Integer pageSize;

        public UserSpecificationBuilder() {
        }

        public UserSpecificationBuilder expired(final boolean isExpired) {
            filters = filters.and(new Filter("expired", ComparisonOperator.EQUALS, isExpired));
            return this;
        }

        public UserSpecificationBuilder locked(final boolean isLocked) {
            filters = filters.and(new Filter("locked", ComparisonOperator.EQUALS, isLocked));
            return this;
        }

        public UserSpecificationBuilder credentialsExpired(final boolean isCredentialsExpired) {
            filters = filters.and(new Filter("credentialsExpired", ComparisonOperator.EQUALS, isCredentialsExpired));
            return this;
        }

        public UserSpecificationBuilder pageNumber(int pageNumber) {
            Assert.isTrue(pageNumber >= 0, "Page number may not be negative.");
            this.pageNumber = pageNumber;
            return this;
        }

        public UserSpecificationBuilder pageSize(int pageSize) {
            Assert.isTrue(pageSize >= 1, "Page size may not be less then 1");
            this.pageSize = pageSize;
            return this;
        }

        public UserSpecification build() {
            return new UserSpecification(this.filters, this.pageNumber, this.pageSize);
        }
    }
}
