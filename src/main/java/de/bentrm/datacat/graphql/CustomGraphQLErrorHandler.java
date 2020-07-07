package de.bentrm.datacat.graphql;

import graphql.GraphQLError;
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;

public class CustomGraphQLErrorHandler extends DefaultGraphQLErrorHandler {

    @Override
    protected boolean isClientError(GraphQLError error) {
        return false;
    }
}
