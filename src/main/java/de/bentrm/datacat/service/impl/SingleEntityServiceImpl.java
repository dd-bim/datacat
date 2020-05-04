package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.repository.EntityRepository;
import de.bentrm.datacat.service.SingleEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class SingleEntityServiceImpl implements SingleEntityService {

    @Autowired
    private EntityRepository entityRepository;

    @Override
    public @NotNull Optional<XtdEntity> findById(@NotBlank String id) {
        return entityRepository.findById(id);
    }
}
