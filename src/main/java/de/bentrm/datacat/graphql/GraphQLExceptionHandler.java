package de.bentrm.datacat.graphql;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable e, @NonNull DataFetchingEnvironment env) {

        if(e instanceof Exception) {
            return toGraphQLError(e);
        } else {
            return super.resolveToSingleError(e, env);
        }
    }
    
        private GraphQLError toGraphQLError(Throwable e) {
        log.warn("Exception while handling request: {}", e.getMessage(), e);
        return GraphqlErrorBuilder.newError().message(e.getMessage()).errorType(ErrorType.DataFetchingException).build();
    }
}
