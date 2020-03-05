package de.bentrm.datacat.graphql;

import de.bentrm.datacat.graphql.fetcher.RelGroupsDataFetchers;
import de.bentrm.datacat.graphql.fetcher.SubjectDataFetchers;
import de.bentrm.datacat.graphql.fetcher.XtdDataFetchers;
import de.bentrm.datacat.graphql.fetcher.XtdObjectDataFetchers;
import de.bentrm.datacat.graphql.resolver.*;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
public class SchemaDefinition implements ResourceLoaderAware {

    Logger logger = LoggerFactory.getLogger(SchemaDefinition.class);

    private ResourceLoader resourceLoader;

    @Autowired
    private XtdDataFetchers dataFetchers;

    @Autowired
    private XtdObjectDataFetchers objectDataFetchers;

    @Autowired
    private SubjectDataFetchers subjectDataFetchers;

    @Autowired
    private RelGroupsDataFetchers relGroupsDataFetchers;

    @Bean
    GraphQLSchema schema() throws IOException {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();

        InputStream input = loadSchema("classpath:schema.graphqls").getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(reader);
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("XtdLanguageRepresentation", typeWiring -> typeWiring
                        .typeResolver(new XtdLanguageRepresentationTypeResolver())
                )
                .type("XtdRoot", typeWiring -> typeWiring
                        .typeResolver(new XtdRootTypeResolver())
                )
                .type("XtdObject", typeWiring -> typeWiring
                        .typeResolver(new XtdObjectTypeResolver())
                )
                .type("XtdCollection", typeWiring -> typeWiring
                        .typeResolver(new XtdCollectionTypeResolver())
                )
                .type("XtdRelationship", typeWiring -> typeWiring
                       .typeResolver(new XtdRelationshipTypeResolver())
                )
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("language", dataFetchers.languageById())
                        .dataFetcher("languages", dataFetchers.languageBySearch())
                        .dataFetcher("document", dataFetchers.externalDocumentById())
                        .dataFetcher("documents", dataFetchers.externalDocumentBySearch())
                        .dataFetcher("subject", objectDataFetchers.subjectById())
                        .dataFetcher("subjects", objectDataFetchers.subjectsByMatch())
                        .dataFetcher("groupsRelation", relGroupsDataFetchers.byId())
                        .dataFetcher("groupsRelations", relGroupsDataFetchers.bySearchOptions())
                )
                .type("XtdName", typeWiring -> typeWiring
                        .dataFetcher("languageName", dataFetchers.languageByLanguageRepresentationId())
                )
                .type("XtdDescription", typeWiring -> typeWiring
                        .dataFetcher("languageName", dataFetchers.languageByLanguageRepresentationId())
                )
                .type("XtdSubject", typeWiring -> typeWiring
                        .dataFetcher("groups", subjectDataFetchers.groups())
                        .dataFetcher("groupedBy", subjectDataFetchers.groupedBy())
                )
                .type("XtdRelGroups", typeWiring -> typeWiring
                        .dataFetcher("relatedObjects", relGroupsDataFetchers.relatedObjects())
                )
                .type("Mutation", typeWiring -> typeWiring
                        .dataFetcher("createLanguage", dataFetchers.createLanguage())
                        .dataFetcher("deleteLanguage", dataFetchers.deleteLanguage())
                        .dataFetcher("createDocument", dataFetchers.createExternalDocument())
                        .dataFetcher("deleteDocument", dataFetchers.deleteExternalDocument())
                        .dataFetcher("createSubject", objectDataFetchers.addSubject())
                        .dataFetcher("deleteSubject", objectDataFetchers.deleteSubject())
                        .dataFetcher("createGroupsRelation", relGroupsDataFetchers.create())
                        .dataFetcher("addRelatedObjectsToGroupsRelation", relGroupsDataFetchers.addRelatedObjects())
                        .dataFetcher("removeRelatedObjectsFromGroupsRelation", relGroupsDataFetchers.removeRelatedObjects())
                        .dataFetcher("deleteGroupsRelation", relGroupsDataFetchers.delete())
                )
                .build();
        return schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    }

    @Override
    public void setResourceLoader(@NotNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource loadSchema(String location) {
        return resourceLoader.getResource(location);
    }
}
