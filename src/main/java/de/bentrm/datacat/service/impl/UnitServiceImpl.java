package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdUnit;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.repository.UnitRepository;
import de.bentrm.datacat.service.UnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class UnitServiceImpl
        extends AbstractRootServiceImpl<XtdUnit, RootInput, RootUpdateInput, UnitRepository>
        implements UnitService {

    public UnitServiceImpl(UnitRepository repository) {
        super(repository);
    }

    @Override
    protected XtdUnit newEntityInstance() {
        return new XtdUnit();
    }
}
