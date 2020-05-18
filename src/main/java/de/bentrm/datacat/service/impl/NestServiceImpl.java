package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.collection.XtdNest;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.repository.NestRepository;
import de.bentrm.datacat.service.NestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class NestServiceImpl
        extends CrudRootServiceImpl<XtdNest, RootInput, RootUpdateInput, NestRepository>
        implements NestService {

    public NestServiceImpl(NestRepository repository) {
        super(repository);
    }

    @Override
    protected XtdNest newEntityInstance() {
        return new XtdNest();
    }
}
