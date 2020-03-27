package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.graphql.dto.TextInput;
import de.bentrm.datacat.repository.object.ObjectRepository;
import de.bentrm.datacat.repository.relationship.RelGroupsRepository;
import de.bentrm.datacat.service.RelGroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class RelGroupsServiceImpl implements RelGroupsService {

    Logger logger = LoggerFactory.getLogger(RelGroupsServiceImpl.class);

    @Autowired
    private RelGroupsRepository entityRepository;

    @Autowired
    private ObjectRepository objectRepository;

    @Transactional
    @Override
    public XtdRelGroups create(@Valid AssociationInput dto) {
        XtdRelGroups entity = toEntity(dto);
        return entityRepository.save(entity);
    }

    @Transactional
    @Override
    public XtdRelGroups update(@Valid AssociationUpdateInput dto) {
        XtdRelGroups entity = entityRepository
                .findByUID(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getId() + " not found."));

        logger.debug("Updating entity {}", entity);

        if (!dto.getRelatingThing().equals(entity.getRelatingThing().getId())) {
            throw new IllegalArgumentException("Relating side of relationship can't be changed. Create a new relationship instead.'");
        }

        // remove things no longer in this relationship
        entity.getRelatedThings().removeIf(thing -> !dto.getRelatedThings().contains(thing.getId()));

        // add new things to this relationship
        Page<XtdObject> relatedThings = objectRepository.findByUIDs(dto.getRelatedThings(), PageRequest.of(0, 1000));
        entity.getRelatedThings().addAll(relatedThings.getContent());

        // general infos
        entity.setVersionId(dto.getVersionId());
        entity.setVersionDate(dto.getVersionDate());

        List<TextInput> nameDtos = dto.getNames();
        List<String> nameIds = nameDtos.stream().map(TextInput::getId).collect(Collectors.toList());
        List<XtdName> names = new ArrayList<>(entity.getNames());

        // empty current set to prepare for updates
        entity.getNames().clear();

        // remove deleted descriptions temporary list
        names.removeIf(x ->  nameIds.indexOf(x.getId()) == -1);

        for (int i = 0; i < nameDtos.size(); i++) {
            TextInput input = nameDtos.get(i);
            XtdName newName = toName(input);
            newName.setSortOrder(i);

            logger.debug("Transient name {}", newName);

            int index = names.indexOf(newName);
            if (input.getId() != null && (index > -1)) {
                // Update of an existing name entity
                XtdName oldName = names.get(index);

                logger.debug("Persistent name to be updated: {}", oldName);

                if (!oldName.getLanguageName().equals(newName.getLanguageName())) {
                    // Update of languageName is not allowed
                    throw new IllegalArgumentException("Update of languageName of name with id " + newName.getId() + " is not allowed.");
                }

                oldName.setName(newName.getName());
                oldName.setSortOrder(newName.getSortOrder());

                logger.debug("Updated persistent name: {}" , oldName);

                entity.getNames().add(oldName);
            } else {
                // New entity with no given id or preset id
                entity.getNames().add(newName);
            }
        }

        List<TextInput> descriptionDtos = dto.getDescriptions();
        List<String> descriptionIds = descriptionDtos.stream().map(TextInput::getId).collect(Collectors.toList());
        List<XtdDescription> descriptions = new ArrayList<>(entity.getDescriptions());

        // empty current set to prepare for updates
        entity.getDescriptions().clear();

        // remove deleted descriptions temporary list
        descriptions.removeIf(x -> descriptionIds.indexOf(x.getId()) == -1);

        for (int i = 0; i < descriptionDtos.size(); i++) {
            TextInput input = descriptionDtos.get(i);
            XtdDescription newDescription = toDescription(input);
            newDescription.setSortOrder(i);

            logger.debug("Transient description {}", newDescription);

            int index = descriptions.indexOf(newDescription);
            if (input.getId() != null && (index > -1)) {
                // Update of an existing entity
                XtdDescription oldDescription = descriptions.get(index);
                logger.debug("Persistent description to be updated: {}", oldDescription);

                if (!oldDescription.getLanguageName().equals(newDescription.getLanguageName())) {
                    // Update of languageName is not allowed
                    throw new IllegalArgumentException("Update of languageName of description with id " + newDescription.getId() + " is not allowed.");
                }

                oldDescription.setDescription(newDescription.getDescription());
                oldDescription.setSortOrder(newDescription.getSortOrder());

                logger.debug("Updated persistent description: {}" , oldDescription);

                entity.getDescriptions().add(oldDescription);
            } else {
                // New entity with no given id
                entity.getDescriptions().add(newDescription);
            }
        }

        logger.debug("New state {}", entity);

        return entityRepository.save(entity);
    }

    @Override
    public Optional<XtdRelGroups> delete(@NotNull String id) {
        Optional<XtdRelGroups> entity = entityRepository.findByUID(id);
        entity.ifPresent(x -> entityRepository.delete(x));
        return entity;
    }

    @Override
    public XtdRelGroups addRelatedObjects(String id, List<String> relatedObjectsIds) {
        XtdRelGroups relation = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No relation with id " + id + " found."));

        for (String relatedObjectId : relatedObjectsIds) {
            XtdObject relatedObject = objectRepository
                    .findByUID(relatedObjectId)
                    .orElseThrow(() -> new IllegalArgumentException("No relatable object with id " + relatedObjectId + " found."));

            // TODO: Throw if related object is already in persistent set
            relation.getRelatedThings().add(relatedObject);
        }

        return entityRepository.save(relation);
    }

    @Override
    public XtdRelGroups removeRelatedObjects(String id, List<String> relatedObjectsIds) {
        XtdRelGroups relation = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No relation with id " + id + " found."));

        for (String relatedObjectId : relatedObjectsIds) {
            XtdObject relatedObject = objectRepository
                    .findByUID(relatedObjectId)
                    .orElseThrow(() -> new IllegalArgumentException("No relatable object with id " + relatedObjectId + " found."));

            if (!relation.getRelatedThings().remove(relatedObject)) {
                throw new IllegalArgumentException("Object with id " + relatedObjectId + " is not related in relation " + id);
            }
        }

        if (relation.getRelatedThings().isEmpty()) {
            throw new IllegalArgumentException("Relation may not be empty");
        }

        return entityRepository.save(relation);
    }

    @Override
    public @NotNull Optional<XtdRelGroups> findById(@NotNull String id) {
        return entityRepository.findByUID(id);
    }

    @Override
    public @NotNull Page<XtdRelGroups> findByIds(@NotNull List<String> ids, @NotNull Pageable pageable) {
        return entityRepository.findByUIDs(ids, pageable);
    }

    @Override
    public @NotNull Page<XtdRelGroups> findAll(@NotNull Pageable pageable) {
        Iterable<XtdRelGroups> nodes = entityRepository.findAll(pageable);
        List<XtdRelGroups> resultList = new ArrayList<>();
        nodes.forEach(resultList::add);
        return PageableExecutionUtils.getPage(resultList, pageable, entityRepository::count);
    }

    @Override
    public @NotNull Page<XtdRelGroups> findByTerm(@NotBlank String term, @NotNull Pageable pageable) {
        return entityRepository.findByTerm(term, pageable);
    }

    @Override
    public Page<XtdRelGroups> findByRelatingObjectId(@NotBlank String relatingObjectId, Pageable pageable) {
        return entityRepository.findByRelatingObjectId(relatingObjectId, pageable);
    }

    @Override
    public @NotNull Page<XtdRelGroups> findByRelatedObjectId(@NotBlank String relatedObjectId, Pageable pageable) {
        return entityRepository.findByRelatedObjectId(relatedObjectId, pageable);
    }

    protected XtdRelGroups toEntity(@Valid AssociationInput input) {
        XtdRelGroups entity = new XtdRelGroups();
        entity.setId(input.getId());
        entity.setVersionId(input.getVersionId());
        entity.setVersionDate(input.getVersionDate());

        XtdObject relating = objectRepository
                .findByUID(input.getRelatingThing())
                .orElseThrow(() -> new IllegalArgumentException("No Object with id " + input.getRelatingThing() + " found."));
        entity.setRelatingThing(relating);

        Page<XtdObject> relatedThings = objectRepository.findByUIDs(input.getRelatedThings(), PageRequest.of(0, 1000));
        entity.getRelatedThings().addAll(relatedThings.getContent());

        List<TextInput> names = input.getNames();
        for (int i = 0; i < names.size(); i++) {
            TextInput name = names.get(i);
            XtdName newName = toName(name);
            newName.setSortOrder(i);
            entity.getNames().add(newName);
        }

        List<TextInput> descriptions = input.getDescriptions();
        for (int i = 0; i < descriptions.size(); i++) {
            TextInput description = descriptions.get(i);
            XtdDescription newDescription = toDescription(description);
            newDescription.setSortOrder(i);
            entity.getDescriptions().add(newDescription);
        }

        return entity;
    }

    protected XtdName toName(TextInput input) {
        if (input.getId() != null) {
            return new XtdName(input.getId(), input.getLanguageCode(), input.getValue());
        }
        return new XtdName(input.getLanguageCode(), input.getValue());
    }

    protected XtdDescription toDescription(TextInput input) {
        if (input.getId() != null) {
            return new XtdDescription(input.getId(), input.getLanguageCode(), input.getValue());
        }
        return new XtdDescription(input.getLanguageCode(), input.getValue());
    }
}
