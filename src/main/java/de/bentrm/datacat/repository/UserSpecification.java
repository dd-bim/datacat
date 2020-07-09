package de.bentrm.datacat.repository;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ToString
public class UserSpecification {

    private final Filters filters;
    private final Integer pageNumber;
    private final Integer pageSize;

    public static UserSpecificationBuilder builder() {
        return new UserSpecificationBuilder();
    }

    private UserSpecification(final Filters filters, final Integer pageNumber, final Integer pageSize) {
        this.filters = filters;
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

    @Slf4j
    @ToString
    public static class UserSpecificationBuilder {

        private List<String> queries;
        private final List<Filter> filters = new ArrayList<>();
        private Integer pageNumber;
        private Integer pageSize;

        public UserSpecificationBuilder() {
        }

        public UserSpecificationBuilder query(final String query) {
            queries = Arrays.stream(query.split("\\s+"))
                    .filter(x -> !x.isBlank())
                    .map(x -> x.contains("*") ? x : x + "*")
                    .collect(Collectors.toList());
            return this;
        }

        public UserSpecificationBuilder expired(final boolean isExpired) {
            filters.add(new Filter("expired", ComparisonOperator.EQUALS, isExpired));
            return this;
        }

        public UserSpecificationBuilder locked(final boolean isLocked) {
            filters.add(new Filter("locked", ComparisonOperator.EQUALS, isLocked));
            return this;
        }

        public UserSpecificationBuilder credentialsExpired(final boolean isCredentialsExpired) {
            filters.add(new Filter("credentialsExpired", ComparisonOperator.EQUALS, isCredentialsExpired));
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
            Filters filterset = new Filters();
            if (queries.size() == 0) {
                filterset.add(this.filters);
            } else {
                for (int i = 0; i < queries.size(); i++) {
                    final Filter textFilter = new Filter("username", ComparisonOperator.LIKE, queries.get(i));
                    if (i == 0) {
                        filterset.add(textFilter);
                    } else {
                        // nb: OR-Operator starts new filter group
                        filterset.or(textFilter);
                    }
                    this.filters.forEach(filterset::and);
                }
            }

            return new UserSpecification(filterset, this.pageNumber, this.pageSize);
        }
    }
}
