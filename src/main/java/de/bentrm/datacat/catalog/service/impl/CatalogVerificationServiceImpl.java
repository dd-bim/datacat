package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.repository.CatalogValidationQuery;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.service.CatalogVerificationService;
import de.bentrm.datacat.catalog.service.value.VerificationValue;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Validated
@Transactional(readOnly = true)
@Service
public class CatalogVerificationServiceImpl implements CatalogVerificationService {

    @Autowired
    private CatalogValidationQuery catalogValidationQuery;

    @Autowired
    private ObjectRepository repository;


    @Override
    public VerificationValue getfindPropGroupWithoutProp() {
        return getLeaves(catalogValidationQuery.findPropGroupWithoutProp());
    }

    @Override
    public VerificationValue getfindPropWithoutSubjectOrPropGroup() {
        return getLeaves(catalogValidationQuery.findPropWithoutSubjectOrPropGroup());
    }

    @Override
    public VerificationValue getfindModelWithoutGroup() {
        return getLeaves(catalogValidationQuery.findModelWithoutGroup());
    }

    @Override
    public VerificationValue getfindGroupWithoutSubject() {
        return getLeaves(catalogValidationQuery.findGroupWithoutSubject());
    }

    @Override
    public VerificationValue getfindSubjectWithoutProp() {
        return getLeaves(catalogValidationQuery.findSubjectWithoutProp());
    }

    @Override
    public VerificationValue getfindValueListWithoutProp() {
        return getLeaves(catalogValidationQuery.findValueListWithoutProp());
    }

    @Override
    public VerificationValue getfindUnitWithoutValueList() {
        return getLeaves(catalogValidationQuery.findUnitWithoutValueList());
    }

    @Override
    public VerificationValue getfindValueWithoutValueList() {
        return getLeaves(catalogValidationQuery.findValueWithoutValueList());
    }

    @Override
    public VerificationValue getfindMissingTags() {
        return getLeaves(catalogValidationQuery.findMissingTags());
    }

    @Override
    public VerificationValue getfindMissingEnglishName() {
        List<String> paths = catalogValidationQuery.findMissingEnglishName();
        return getLeaves(paths);
    }

    @Override
    public VerificationValue getfindMultipleIDs() {
        List<String> paths = catalogValidationQuery.findMultipleIDs();
        return getLeaves(paths);
    }

    @Override
    public VerificationValue getfindMissingDescription() {
        List<String> paths = catalogValidationQuery.findMissingDescription();
        return getLeaves(paths);
    }

    @Override
    public VerificationValue getfindMissingEnglishDescription() {
        List<String> paths = catalogValidationQuery.findMissingEnglishDescription();
        return getLeaves(paths);
    }

    @Override
    public VerificationValue getfindMultipleNames() {
        List<String> paths = catalogValidationQuery.findMultipleNames();
        return getLeaves(paths);
    }

    @Override
    public VerificationValue getfindMultipleNamesAcrossClasses() {
        List<String> paths = catalogValidationQuery.findMultipleNamesAcrossClasses();
        return getLeaves(paths);
    }

    public VerificationValue getLeaves(List<String> paths) {
        final Iterable<XtdObject> nodes = repository.findAllEntitiesById(paths);
        final List<XtdObject> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());
        return new VerificationValue(leaves, paths);
    }
}
