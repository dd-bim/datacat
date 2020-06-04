package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.graphql.dto.TextInput;
import de.bentrm.datacat.repository.GraphEntityRepository;
import de.bentrm.datacat.service.CrudEntityService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CrudRootServiceImpl<
            T extends XtdRoot,
            C extends RootInput,
            U extends RootUpdateInput,
            R extends GraphEntityRepository<T>
        >
        extends CrudEntityServiceImpl<T, C, U, R>
        implements CrudEntityService<T, C, U> {


    public CrudRootServiceImpl(R repository) {
        super(repository);
    }

    @Override
    protected void setEntityProperties(T entity, C dto) {
        super.setEntityProperties(entity, dto);

        entity.setVersionId(dto.getVersionId());
        entity.setVersionDate(dto.getVersionDate());

        List<TextInput> descriptions = dto.getDescriptions();
        for (int i = 0; i < descriptions.size(); i++) {
            TextInput description = descriptions.get(i);
            XtdDescription newDescription = newDescriptionInstance(description);
            newDescription.setSortOrder(i);
            entity.getDescriptions().add(newDescription);
        }
    }

    @Override
    protected void updateEntityProperties(T entity, U dto) {
        super.updateEntityProperties(entity, dto);

        entity.setVersionId(dto.getVersionId());
        entity.setVersionDate(dto.getVersionDate());

        List<TextInput> descriptionDtos = dto.getDescriptions();
        List<String> descriptionIds = descriptionDtos.stream().map(TextInput::getId).collect(Collectors.toList());
        List<XtdDescription> descriptions = new ArrayList<>(entity.getDescriptions());

        // empty current set to prepare for updates
        entity.getDescriptions().clear();

        // remove deleted descriptions temporary list
        descriptions.removeIf(x -> !descriptionIds.contains(x.getId()));

        for (int i = 0; i < descriptionDtos.size(); i++) {
            TextInput input = descriptionDtos.get(i);
            XtdDescription newDescription = newDescriptionInstance(input);
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

                oldDescription.setValue(newDescription.getValue());
                oldDescription.setSortOrder(newDescription.getSortOrder());

                logger.debug("Updated persistent description: {}" , oldDescription);

                entity.getDescriptions().add(oldDescription);
            } else {
                // New entity with no given id
                entity.getDescriptions().add(newDescription);
            }
        }
    }

    @Transactional
    @Override
    public Optional<T> delete(@NotNull String id) {
        Optional<T> result = repository.findById(id);

        if (result.isPresent()) {
            T entity = result.get();

            // delete groups relationships of this entity
            entity.getGroups().forEach(relation -> {
                logger.debug("Deleting Relationship {}", relation);
                relGroupsService.delete(relation.getId());
            });

            repository.delete(entity);
        }

        return result;
    }

    protected XtdDescription newDescriptionInstance(TextInput input) {
        if (input.getId() != null) {
            return new XtdDescription(input.getId(), input.getLanguageCode(), input.getValue());
        }
        return new XtdDescription(input.getLanguageCode(), input.getValue());
    }
}
