package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.repository.BagRepository;
import de.bentrm.datacat.service.BagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class BagServiceImpl
        extends CrudRootServiceImpl<XtdBag, RootInput, RootUpdateInput, BagRepository>
        implements BagService {

    public BagServiceImpl(BagRepository repository) {
        super(repository);
    }

    @Override
    protected XtdBag newEntityInstance() {
        return new XtdBag();
    }
}
