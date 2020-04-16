package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.graphql.dto.TextInput;
import de.bentrm.datacat.repository.GraphEntityRepository;
import de.bentrm.datacat.service.RelGroupsService;
import de.bentrm.datacat.service.RootService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Transactional(readOnly = true)
public abstract class AbstractRootServiceImpl<
		T extends XtdRoot,
		C extends RootInput,
		U extends RootUpdateInput, R extends GraphEntityRepository<T, String>>
		implements RootService<T, C, U> {

	Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

	protected final R repository;

	@Autowired
	protected RelGroupsService relGroupsService;

	public AbstractRootServiceImpl(R repository) {
		this.repository = repository;
	}

	@Transactional
	@Override
	public T create(@Valid C dto) {
		T newEntity = newEntityInstance();
		setEntityProperties(newEntity, dto);
		return repository.save(newEntity);
	}

	protected void setEntityProperties(T entity, C dto) {
		entity.setId(dto.getId());
		entity.setVersionId(dto.getVersionId());
		entity.setVersionDate(dto.getVersionDate());

		List<TextInput> names = dto.getNames();
		for (int i = 0; i < names.size(); i++) {
			TextInput name = names.get(i);
			XtdName newName = newNameInstance(name);
			newName.setSortOrder(i);
			entity.getNames().add(newName);
		}

		List<TextInput> descriptions = dto.getDescriptions();
		for (int i = 0; i < descriptions.size(); i++) {
			TextInput description = descriptions.get(i);
			XtdDescription newDescription = newDescriptionInstance(description);
			newDescription.setSortOrder(i);
			entity.getDescriptions().add(newDescription);
		}
	}

	@Transactional
	@Override
	public T update(@Valid U dto) {
		T entity = repository
				.findById(dto.getId())
				.orElseThrow(() -> new IllegalArgumentException("No Object with id " + dto.getId() + " not found."));

		updateEntityProperties(entity, dto);

		return repository.save(entity);
	}

	protected void updateEntityProperties(T entity, U dto) {
		logger.debug("Updating entity {}", entity);

		// general infos
		entity.setVersionId(dto.getVersionId());
		entity.setVersionDate(dto.getVersionDate());

		List<TextInput> nameDtos = dto.getNames();
		List<String> nameIds = nameDtos.stream().map(TextInput::getId).collect(Collectors.toList());
		List<XtdName> names = new ArrayList<>(entity.getNames());

		// empty current set to prepare for updates
		entity.getNames().clear();

		// remove deleted descriptions temporary list
		names.removeIf(x -> !nameIds.contains(x.getId()));

		for (int i = 0; i < nameDtos.size(); i++) {
			TextInput input = nameDtos.get(i);
			XtdName newName = newNameInstance(input);
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

	@Override
	public @NotNull Optional<T> findById(@NotNull String id) {
		return repository.findById(id);
	}

	@Override
	public @NotNull Page<T> findByIds(@NotNull List<String> ids, @NotNull Pageable pageable) {
		return repository.findAllById(ids, pageable);
	}

	@Override
	public @NotNull Page<T> findAll(@NotNull Pageable pageable) {
		Iterable<T> nodes = repository.findAll(pageable);
		List<T> resultList = new ArrayList<>();
		nodes.forEach(resultList::add);
		return PageableExecutionUtils.getPage(resultList, pageable, repository::count);
	}

	@Override
	public @NotNull Page<T> findByTerm(@NotBlank String term, @NotNull Pageable pageable) {
		return repository.findAllByTerm(term, pageable);
	}

	protected abstract T newEntityInstance();

	protected XtdName newNameInstance(TextInput input) {
		if (input.getId() != null) {
			return new XtdName(input.getId(), input.getLanguageCode(), input.getValue());
		}
		return new XtdName(input.getLanguageCode(), input.getValue());
	}

	protected XtdDescription newDescriptionInstance(TextInput input) {
		if (input.getId() != null) {
			return new XtdDescription(input.getId(), input.getLanguageCode(), input.getValue());
		}
		return new XtdDescription(input.getLanguageCode(), input.getValue());
	}
}
