package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.*;
import de.bentrm.datacat.catalog.service.CatalogVerificationService;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
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
    private RootRepository rootRepository;


    @Override
    public HierarchyValue getfindPropGroupWithoutProp() {

        List<List<String>> paths = catalogValidationQuery.findPropGroupWithoutProp();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindPropWithoutSubjectOrPropGroup() {

        List<List<String>> paths = catalogValidationQuery.findPropWithoutSubjectOrPropGroup();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindModelWithoutGroup() {

        List<List<String>> paths = catalogValidationQuery.findModelWithoutGroup();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindGroupWithoutSubject() {

        List<List<String>> paths = catalogValidationQuery.findGroupWithoutSubject();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindSubjectWithoutProp() {

        List<List<String>> paths = catalogValidationQuery.findSubjectWithoutProp();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindMeasureWithoutProp() {

        List<List<String>> paths = catalogValidationQuery.findMeasureWithoutProp();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindUnitWithoutMeasure() {

        List<List<String>> paths = catalogValidationQuery.findUnitWithoutMeasure();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindValueWithoutMeasure() {

        List<List<String>> paths = catalogValidationQuery.findValueWithoutMeasure();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindMissingTags(CatalogRecordSpecification rootNodeSpecification) {
        List<List<String>> paths = catalogValidationQuery.findMissingTags();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindMissingEnglishName(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMissingEnglishName();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindMultipleIDs(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMultipleIDs();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindMissingDescription(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMissingDescription();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindMissingEnglishDescription(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMissingEnglishDescription();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindMultipleNames(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMultipleNames();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }

    @Override
    public HierarchyValue getfindMultipleNamesAcrossClasses(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMultipleNamesAcrossClasses();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }
}
