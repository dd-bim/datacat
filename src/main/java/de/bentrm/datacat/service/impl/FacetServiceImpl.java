package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.Facet;
import de.bentrm.datacat.domain.Translation;
import de.bentrm.datacat.graphql.dto.DtoMapper;
import de.bentrm.datacat.graphql.dto.TextInput;
import de.bentrm.datacat.repository.FacetRepository;
import de.bentrm.datacat.service.FacetService;
import de.bentrm.datacat.service.Specification;
import de.bentrm.datacat.service.dto.FacetInput;
import de.bentrm.datacat.service.dto.FacetUpdateInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static de.bentrm.datacat.service.impl.ServiceUtil.mapTextInputToTranslationSet;

@Validated
@Transactional(readOnly = true)
@Service
public class FacetServiceImpl implements FacetService {

    private final FacetRepository facetRepository;

    private final DtoMapper dtoMapper;

    public FacetServiceImpl(FacetRepository facetRepository, DtoMapper dtoMapper) {
        this.facetRepository = facetRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public Facet create(@Valid FacetInput dto) {
        Facet facet = new Facet();

        facet.setId(dto.getId());

        for (TextInput input : dto.getNames()) {
            final Translation translation = dtoMapper.toTranslation(input);
            facet.getNames().add(translation);
        }

        for (TextInput input : dto.getDescriptions()) {
            final Translation translation = dtoMapper.toTranslation(input);
            facet.getDescriptions().add(translation);
        }

        facet.getTargets().addAll(dto.getTargets());
        return facetRepository.save(facet);
    }

    @Override
    public Facet update(@Valid FacetUpdateInput dto) {
        final Facet facet = facetRepository
                .findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("No facet with id " + dto.getId() + " found."));

        mapTextInputToTranslationSet(facet.getNames(), dto.getNames());
        mapTextInputToTranslationSet(facet.getDescriptions(), dto.getDescriptions());
        facet.getTargets().addAll(dto.getTargets());

        return facetRepository.save(facet);
    }

    @Override
    public Optional<Facet> delete(@NotNull String id) {
        Optional<Facet> result = facetRepository.findById(id);
        result.ifPresent(facetRepository::delete);
        return result;
    }

    @Override
    public @NotNull Optional<Facet> findById(@NotNull String id) {
        return facetRepository.findById(id);
    }

    @Override
    public @NotNull Page<Facet> search(@NotNull Specification specification) {
        return facetRepository.findAll(specification);
    }

    @Override
    public @NotNull Page<Facet> findByIds(@NotNull List<String> ids, @NotNull Pageable pageable) {
        final Specification spec = Specification.unspecified();
        spec.setEntityTypeIn(List.of("Facet"));
        spec.setPageSize(pageable.getPageSize());
        spec.setPageNumber(pageable.getPageNumber());
        return facetRepository.findAll(spec);
    }
}
