package de.bentrm.datacat.graphql;

// import graphql.GraphQLError;
// import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;

// public class CustomGraphQLErrorHandler extends DefaultGraphQLErrorHandler {

//     @Override
//     protected boolean isClientError(GraphQLError error) {
//         return false;
//     }
// }

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Map;

public class CustomGraphQLErrorHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable exception, @NonNull DataFetchingEnvironment env) {

        return new GraphQLError() {
            @Override
            public String getMessage() {
                return exception.getMessage();
            }

            @Override
            public List<Object> getPath() {
                return env.getExecutionStepInfo().getPath().toList();
            }

            @Override
            public Map<String, Object> toSpecification() {
                return Map.of(
                    "message", getMessage(),
                    "path", getPath(),
                    "extensions", Map.of("errorType", ErrorType.INTERNAL_ERROR)
                );
            }

            @Override
            public List<SourceLocation> getLocations() {
                return List.of(new SourceLocation(env.getField().getSourceLocation().getLine(), env.getField().getSourceLocation().getColumn()));
            }

            @Override
            public ErrorClassification getErrorType() {
                return ErrorType.INTERNAL_ERROR;
            }
        };
    }
}