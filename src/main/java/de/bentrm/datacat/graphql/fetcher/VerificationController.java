package de.bentrm.datacat.graphql.fetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import de.bentrm.datacat.catalog.service.CatalogVerificationService;
import de.bentrm.datacat.catalog.service.value.VerificationConnection;

@Controller
public class VerificationController {
    
    @Autowired
    private CatalogVerificationService service;

    @QueryMapping
    public VerificationConnection findSubjectWithoutProp(@Argument int pageNumber, @Argument int pageSize) {
        return service.getSubjectWithoutProp(PageRequest.of(pageNumber, pageSize));
    }

    @QueryMapping
    public VerificationConnection findThemeWithoutSubject(@Argument int pageNumber, @Argument int pageSize) {
        return service.getThemeWithoutSubject(PageRequest.of(pageNumber, pageSize));
    }

    @QueryMapping
    public VerificationConnection findPropGroupWithoutProp(@Argument int pageNumber, @Argument int pageSize) {
        return service.getPropGroupWithoutProp(PageRequest.of(pageNumber, pageSize));
    }

    @QueryMapping
    public VerificationConnection findPropWithoutSubjectOrPropGroup(@Argument int pageNumber, @Argument int pageSize) {
        return service.getPropWithoutSubjectOrPropGroup(PageRequest.of(pageNumber, pageSize));
    }

    @QueryMapping
    public VerificationConnection findValueListWithoutProp(@Argument int pageNumber, @Argument int pageSize) {
        return service.getValueListWithoutProp(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findUnitWithoutValueList(@Argument int pageNumber, @Argument int pageSize) {
        return service.getUnitWithoutValueList(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findValueWithoutValueList(@Argument int pageNumber, @Argument int pageSize) {
        return service.getValueWithoutValueList(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findMissingTags(@Argument int pageNumber, @Argument int pageSize) {
        return service.getMissingTags(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findMissingEnglishName(@Argument int pageNumber, @Argument int pageSize) {
        return service.getMissingEnglishName(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findMultipleIDs(@Argument int pageNumber, @Argument int pageSize) {
        return service.getMultipleIDs(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findMissingDescription(@Argument int pageNumber, @Argument int pageSize) {
        return service.getMissingDescription(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findMissingEnglishDescription(@Argument int pageNumber, @Argument int pageSize) {
        return service.getMissingEnglishDescription(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findMultipleNames(@Argument int pageNumber, @Argument int pageSize) {
        return service.getMultipleNames(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findMultipleNamesAcrossClasses(@Argument int pageNumber, @Argument int pageSize) {
        return service.getMultipleNamesAcrossClasses(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findMissingDictionary(@Argument int pageNumber, @Argument int pageSize) {
        return service.getMissingDictionary(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findMissingReferenceDocument(@Argument int pageNumber, @Argument int pageSize) {
        return service.getMissingReferenceDocument(PageRequest.of(pageNumber, pageSize));
    }
    @QueryMapping
    public VerificationConnection findInactiveConcepts(@Argument int pageNumber, @Argument int pageSize) {
        return service.getInactiveConcepts(PageRequest.of(pageNumber, pageSize));
    }
}
