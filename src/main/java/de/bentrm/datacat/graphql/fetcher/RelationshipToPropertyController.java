package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.service.RelationshipToPropertyRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class RelationshipToPropertyController {


    @Autowired
    private RelationshipToPropertyRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdRelationshipToProperty> getRelationshipToProperty(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdRelationshipToProperty.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdRelationshipToProperty> findRelationshipToProperties(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdRelationshipToProperty> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdRelationshipToProperty", field = "connectingProperty")
    public Map<XtdRelationshipToProperty, XtdProperty> getConnectingProperty(List<XtdRelationshipToProperty> relationshipToProperties) {
        return relationshipToProperties.stream()
                .filter(relationshipToProperty -> relationshipToProperty != null)  // Filter out null relationships
                .collect(Collectors.toMap(
                        relationshipToProperty -> relationshipToProperty,
                        relationshipToProperty -> {
                            XtdProperty result = service.getConnectingProperty(relationshipToProperty);
                            return result;  // Return as-is since it's not Optional or List
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdRelationshipToProperty", field = "targetProperties")
    public Map<XtdRelationshipToProperty, List<XtdProperty>> getTargetProperties(List<XtdRelationshipToProperty> relationshipToProperties) {
        return relationshipToProperties.stream()
                .filter(relationshipToProperty -> relationshipToProperty != null)  // Filter out null relationships
                .collect(Collectors.toMap(
                        relationshipToProperty -> relationshipToProperty,
                        relationshipToProperty -> {
                            List<XtdProperty> result = service.getTargetProperties(relationshipToProperty);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }
}
