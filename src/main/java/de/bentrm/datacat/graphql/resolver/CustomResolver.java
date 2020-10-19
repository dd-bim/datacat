package de.bentrm.datacat.graphql.resolver;

import graphql.schema.TypeResolver;

public interface CustomResolver extends TypeResolver {
    String getTypeName();
}
