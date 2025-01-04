package de.bentrm.datacat.graphql.fetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import de.bentrm.datacat.catalog.service.CatalogVerificationService;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.input.VerificationFilterInput;
import de.bentrm.datacat.graphql.input.VerificationNodeTypeFilterInput;

@Controller
public class VerificationController {
    
    @Autowired
    private CatalogVerificationService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public HierarchyValue findSubjectWithoutProp() {
        return service.getfindSubjectWithoutProp();
    }

    @QueryMapping
    public HierarchyValue findGroupWithoutSubject() {
        return service.getfindGroupWithoutSubject();
    }

    @QueryMapping
    public HierarchyValue findPropGroupWithoutProp() {
        return service.getfindPropGroupWithoutProp();
    }

    @QueryMapping
    public HierarchyValue findPropWithoutSubjectOrPropGroup() {
        return service.getfindPropWithoutSubjectOrPropGroup();
    }

    @QueryMapping
    public HierarchyValue findModelWithoutGroup() {
        return service.getfindModelWithoutGroup();
    }

    @QueryMapping
    public HierarchyValue findMeasureWithoutProp() {
        return service.getfindMeasureWithoutProp();
    }   

    @QueryMapping
    public HierarchyValue findUnitWithoutMeasure() {
        return service.getfindUnitWithoutMeasure();
    }

    @QueryMapping
    public HierarchyValue findValueWithoutMeasure() {
        return service.getfindValueWithoutMeasure();
    }   

    @QueryMapping
    public HierarchyValue findMissingTags(@Argument VerificationFilterInput input) {
        VerificationNodeTypeFilterInput nodeTypeFilter = input.getNodeTypeFilter();
        CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(nodeTypeFilter);
        return service.getfindMissingTags(specification);
    }

    @QueryMapping
    public HierarchyValue findMissingEnglishName(@Argument VerificationFilterInput input) {
        VerificationNodeTypeFilterInput nodeTypeFilter = input.getNodeTypeFilter();
        CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(nodeTypeFilter);
        return service.getfindMissingEnglishName(specification);
    }

    @QueryMapping
    public HierarchyValue findMultipleIDs(@Argument VerificationFilterInput input) {
        VerificationNodeTypeFilterInput nodeTypeFilter = input.getNodeTypeFilter();
        CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(nodeTypeFilter);
        return service.getfindMultipleIDs(specification);
    }

    @QueryMapping
    public HierarchyValue findMissingDescription(@Argument VerificationFilterInput input) {
        VerificationNodeTypeFilterInput nodeTypeFilter = input.getNodeTypeFilter();
        CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(nodeTypeFilter);
        return service.getfindMissingDescription(specification);
    }

    @QueryMapping
    public HierarchyValue findMissingEnglishDescription(@Argument VerificationFilterInput input) {
        VerificationNodeTypeFilterInput nodeTypeFilter = input.getNodeTypeFilter();
        CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(nodeTypeFilter);
        return service.getfindMissingEnglishDescription(specification);
    }

    @QueryMapping
    public HierarchyValue findMultipleNames(@Argument VerificationFilterInput input) {
        VerificationNodeTypeFilterInput nodeTypeFilter = input.getNodeTypeFilter();
        CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(nodeTypeFilter);
        return service.getfindMultipleNames(specification);
    }

    @QueryMapping
    public HierarchyValue findMultipleNamesAcrossClasses(@Argument VerificationFilterInput input) {
        VerificationNodeTypeFilterInput nodeTypeFilter = input.getNodeTypeFilter();
        CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(nodeTypeFilter);
        return service.getfindMultipleNamesAcrossClasses(specification);
    }
}
