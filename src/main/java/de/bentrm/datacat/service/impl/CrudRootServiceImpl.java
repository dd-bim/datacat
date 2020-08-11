package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.graphql.dto.TextInput;
import de.bentrm.datacat.repository.GraphEntityRepository;
import de.bentrm.datacat.service.CrudEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class CrudRootServiceImpl<
        T extends XtdRoot,
        C extends RootInput,
        U extends RootUpdateInput,
        R extends GraphEntityRepository<T>
        >
        extends CatalogItemServiceImpl<T, C, U, R>
        implements CrudEntityService<T, C, U> {


    public CrudRootServiceImpl(R repository) {
        super(repository);
    }

    @Override
    protected void setEntityProperties(T entity, C dto) {
        super.setEntityProperties(entity, dto);

        entity.setVersionId(dto.getVersionId());
        entity.setVersionDate(dto.getVersionDate());

        for (TextInput input : dto.getDescriptions()) {
            entity.setDescription(input.getId(), input.getLanguageCode(), List.of(input.getValue()));
        }
    }

    @Override
    protected void updateEntityProperties(T entity, U dto) {
        super.updateEntityProperties(entity, dto);

        entity.setVersionId(dto.getVersionId());
        entity.setVersionDate(dto.getVersionDate());

        List<TextInput> descriptions = dto.getDescriptions();
        for (TextInput input : descriptions) {
            entity.setDescription(input.getId(), input.getLanguageCode(), List.of(input.getValue()));
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
                log.debug("Deleting Relationship {}", relation);
                relGroupsService.delete(relation.getId());
            });

            repository.delete(entity);
        }

        return result;
    }
}
