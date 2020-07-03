package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Translation;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.graphql.dto.TextInput;
import de.bentrm.datacat.repository.GraphEntityRepository;
import de.bentrm.datacat.service.CrudEntityService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static de.bentrm.datacat.service.impl.ServiceUtil.mapTextInputToTranslationSet;

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

        List<TextInput> descriptions = dto.getDescriptions();
        for (TextInput input : descriptions) {
            final Translation translation = inputMapper.toTranslation(input);
            entity.getDescriptions().add(translation);
        }
    }

    @Override
    protected void updateEntityProperties(T entity, U dto) {
        super.updateEntityProperties(entity, dto);

        entity.setVersionId(dto.getVersionId());
        entity.setVersionDate(dto.getVersionDate());

        mapTextInputToTranslationSet(entity.getDescriptions(), dto.getDescriptions());
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
}
