package de.bentrm.datacat.query;

import de.bentrm.datacat.domain.PropertyQueryHint;
import org.apache.commons.text.StringSubstitutor;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCustomQuery<T> implements CustomQuery {

    public static final Sort DEFAULT_SORT_ORDER = Sort.by(Sort.Order.asc("name"));
    private static final Logger logger = LoggerFactory.getLogger(AbstractCustomQuery.class);
    public final int DEFAULT_PAGE_NUMBER = 1;
    public final int DEFAULT_PAGE_SIZE = 10;
    protected final Class<T> entityType;
    protected final Session session;
    protected final Map<String, Object> queryParameters = new HashMap<>();

    public AbstractCustomQuery(Class<T> entityType, Session session) {
        this.entityType = entityType;
        this.session = session;
    }

    protected @NotNull String getNodeEntityLabel() {
        NodeEntity annotation = entityType.getAnnotation(NodeEntity.class);
        return annotation.label();
    }

    @Override
    public  @NotNull String getPropertyAggregations() {
        // TODO: Make sure this does not backfire with bad performance
        PropertyQueryHint annotation = entityType.getAnnotation(PropertyQueryHint.class);
        String[] values = annotation.value();

        for (int i = 0; i < values.length; i++) {
            String label = "p" + i;
            values[i] = "[ " + label + "=" + values[i] + " | [relationships(" + label + "), nodes(" + label + ")] ]";
        }

        return String.join(", ", values);
    }

    public @NotNull Map<String, Object> getSubstitutionParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("label", this.getNodeEntityLabel());
        parameters.put("propertyAggregations", this.getPropertyAggregations());
        return parameters;
    }

    public @NotNull String prepareCypherQuery() {
        StringSubstitutor substitutor = new StringSubstitutor(getSubstitutionParameters());
        String query = substitutor.replace(getQueryTemplate());
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

}
