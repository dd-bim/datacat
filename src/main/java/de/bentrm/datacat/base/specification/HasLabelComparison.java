package de.bentrm.datacat.base.specification;

import lombok.Data;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.PropertyValueTransformer;
import org.neo4j.ogm.cypher.function.FilterFunction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Custom Neo4j ORM filter function to find database entities denoted by
 * a label.
 */
@Data
public class HasLabelComparison implements FilterFunction<Object> {

    protected static final String PARAMETER_NAME = "property";

    private final Object value;
    private Filter filter;

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String expression(String nodeIdentifier) {
        return String.format("ANY(collectionFields IN $`%s` WHERE collectionFields in labels(%s)) ",
                filter.uniqueParameterName(), nodeIdentifier);
    }

    @Override
    public String expression(String nodeIdentifier, String filteredProperty, UnaryOperator<String> createUniqueParameterName) {
        return String.format("ANY(collectionFields IN $`%s` WHERE collectionFields in labels(%s)) ",
                createUniqueParameterName.apply(PARAMETER_NAME), nodeIdentifier);
    }

    @Override
    public Map<String, Object> parameters() {
        Map<String, Object> map = new HashMap<>();
        map.put(filter.uniqueParameterName(), filter.getTransformedPropertyValue());
        return map;
    }

    @Override
    public Map<String, Object> parameters(UnaryOperator<String> createUniqueParameterName, PropertyValueTransformer propertyValueTransformer) {
        return Collections.singletonMap(createUniqueParameterName.apply(PARAMETER_NAME),
                propertyValueTransformer.transformPropertyValue(this.value));
    }
}
