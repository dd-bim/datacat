package de.bentrm.datacat.graphql.fetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import de.bentrm.datacat.catalog.service.CatalogVerificationService;
import de.bentrm.datacat.catalog.service.value.VerificationValue;

@Controller
public class VerificationController {
    
    @Autowired
    private CatalogVerificationService service;

    @QueryMapping
    public VerificationValue findSubjectWithoutProp() {
        return service.getfindSubjectWithoutProp();
    }

    @QueryMapping
    public VerificationValue findGroupWithoutSubject() {
        return service.getfindGroupWithoutSubject();
    }

    @QueryMapping
    public VerificationValue findPropGroupWithoutProp() {
        return service.getfindPropGroupWithoutProp();
    }

    @QueryMapping
    public VerificationValue findPropWithoutSubjectOrPropGroup() {
        return service.getfindPropWithoutSubjectOrPropGroup();
    }

    @QueryMapping
    public VerificationValue findModelWithoutGroup() {
        return service.getfindModelWithoutGroup();
    }

    @QueryMapping
    public VerificationValue findValueListWithoutProp() {
        return service.getfindValueListWithoutProp();
    }   

    @QueryMapping
    public VerificationValue findUnitWithoutValueList() {
        return service.getfindUnitWithoutValueList();
    }

    @QueryMapping
    public VerificationValue findValueWithoutValueList() {
        return service.getfindValueWithoutValueList();
    }   

    @QueryMapping
    public VerificationValue findMissingTags() {
        return service.getfindMissingTags();
    }

    @QueryMapping
    public VerificationValue findMissingEnglishName() {
        return service.getfindMissingEnglishName();
    }

    @QueryMapping
    public VerificationValue findMultipleIDs() {
        return service.getfindMultipleIDs();
    }

    @QueryMapping
    public VerificationValue findMissingDescription() {
        return service.getfindMissingDescription();
    }

    @QueryMapping
    public VerificationValue findMissingEnglishDescription() {
        return service.getfindMissingEnglishDescription();
    }

    @QueryMapping
    public VerificationValue findMultipleNames() {
        return service.getfindMultipleNames();
    }

    @QueryMapping
    public VerificationValue findMultipleNamesAcrossClasses() {
        return service.getfindMultipleNamesAcrossClasses();
    }
}
