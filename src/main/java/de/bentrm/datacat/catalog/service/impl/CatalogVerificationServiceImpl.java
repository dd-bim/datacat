package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.*;
import de.bentrm.datacat.catalog.service.CatalogVerificationService;
import de.bentrm.datacat.catalog.service.value.verification.*;
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
    public findPropGroupWithoutPropValue getfindPropGroupWithoutProp() {

        List<List<String>> paths = catalogValidationQuery.findPropGroupWithoutProp();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findPropGroupWithoutPropValue(leaves, paths);
    }

    @Override
    public findPropWithoutSubjectOrPropGroupValue getfindPropWithoutSubjectOrPropGroup() {

        List<List<String>> paths = catalogValidationQuery.findPropWithoutSubjectOrPropGroup();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findPropWithoutSubjectOrPropGroupValue(leaves, paths);
    }

    @Override
    public findModelWithoutGroupValue getfindModelWithoutGroup() {

        List<List<String>> paths = catalogValidationQuery.findModelWithoutGroup();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findModelWithoutGroupValue(leaves, paths);
    }

    @Override
    public findGroupWithoutSubjectValue getfindGroupWithoutSubject() {

        List<List<String>> paths = catalogValidationQuery.findGroupWithoutSubject();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findGroupWithoutSubjectValue(leaves, paths);
    }

    @Override
    public findSubjectWithoutPropValue getfindSubjectWithoutProp() {

        List<List<String>> paths = catalogValidationQuery.findSubjectWithoutProp();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findSubjectWithoutPropValue(leaves, paths);
    }

    @Override
    public findMeasureWithoutPropValue getfindMeasureWithoutProp() {

        List<List<String>> paths = catalogValidationQuery.findMeasureWithoutProp();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findMeasureWithoutPropValue(leaves, paths);
    }

    @Override
    public findUnitWithoutMeasureValue getfindUnitWithoutMeasure() {

        List<List<String>> paths = catalogValidationQuery.findUnitWithoutMeasure();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findUnitWithoutMeasureValue(leaves, paths);
    }

    @Override
    public findValueWithoutMeasureValue getfindValueWithoutMeasure() {

        List<List<String>> paths = catalogValidationQuery.findValueWithoutMeasure();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findValueWithoutMeasureValue(leaves, paths);
    }

    @Override
    public findMissingTagsValue getfindMissingTags(CatalogRecordSpecification rootNodeSpecification) {
        List<List<String>> paths = catalogValidationQuery.findMissingTags();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findMissingTagsValue(leaves, paths);
    }

    @Override
    public findMissingEnglishNameValue getfindMissingEnglishName(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMissingEnglishName();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findMissingEnglishNameValue(leaves, paths);
    }

    @Override
    public findMultipleIDsValue getfindMultipleIDs(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMultipleIDs();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findMultipleIDsValue(leaves, paths);
    }

    @Override
    public findMissingDescriptionValue getfindMissingDescription(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMissingDescription();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findMissingDescriptionValue(leaves, paths);
    }

    @Override
    public findMissingEnglishDescriptionValue getfindMissingEnglishDescription(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMissingEnglishDescription();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findMissingEnglishDescriptionValue(leaves, paths);
    }

    @Override
    public findMultipleNamesValue getfindMultipleNames(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMultipleNames();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findMultipleNamesValue(leaves, paths);
    }

    @Override
    public findMultipleNamesAcrossClassesValue getfindMultipleNamesAcrossClasses(CatalogRecordSpecification rootNodeSpecification) {

        List<List<String>> paths = catalogValidationQuery.findMultipleNamesAcrossClasses();

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new findMultipleNamesAcrossClassesValue(leaves, paths);
    }
}
