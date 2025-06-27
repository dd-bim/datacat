package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.repository.CatalogValidationQuery;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.service.CatalogVerificationService;
import de.bentrm.datacat.catalog.service.value.VerificationConnection;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    public VerificationConnection getSubjectWithoutProp(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findSubjectWithoutProp();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getThemeWithoutSubject(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findThemeWithoutSubject();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getPropGroupWithoutProp(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findPropGroupWithoutProp();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getPropWithoutSubjectOrPropGroup(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findPropWithoutSubjectOrPropGroup();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getValueListWithoutProp(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findValueListWithoutProp();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getUnitWithoutValueList(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findUnitWithoutValueList();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getValueWithoutValueList(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findValueWithoutValueList();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getMissingTags(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findMissingTags();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getMissingEnglishName(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findMissingEnglishName();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getMultipleIDs(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findMultipleIDs();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getMissingDescription(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findMissingDescription();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getMissingEnglishDescription(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findMissingEnglishDescription();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getMultipleNames(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findMultipleNames();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getMultipleNamesAcrossClasses(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findMultipleNamesAcrossClasses();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getMissingDictionary(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findMissingDictionary();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getMissingReferenceDocument(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findMissingReferenceDocument();
        return getPagedLeaves(paths, pageable);
    }

    @Override
    public VerificationConnection getInactiveConcepts(Pageable pageable) {
        List<String> paths = catalogValidationQuery.findInactiveConcepts();
        return getPagedLeaves(paths, pageable);
    }

    private VerificationConnection getPagedLeaves(List<String> paths, Pageable pageable) {
        int total = paths.size();
        int from = (int) pageable.getOffset();
        int to = Math.min(from + pageable.getPageSize(), total);
        List<String> pagedPaths = paths.subList(Math.min(from, total), Math.min(to, total));
        final Iterable<XtdObject> nodes = repository.findAllEntitiesById(pagedPaths);
        final List<XtdObject> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());
        return new VerificationConnection(leaves, pagedPaths, new de.bentrm.datacat.graphql.PageInfo(pageable.getPageNumber(), pageable.getPageSize(), pagedPaths.size(), (long) Math.ceil((double) total / pageable.getPageSize())), (long) total);
    }
}
