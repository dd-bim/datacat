package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.repository.MeasureWithUnitRepository;
import de.bentrm.datacat.service.MeasureWithUnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class MeasureWithUnitServiceImpl
        extends CrudEntityServiceImpl<XtdMeasureWithUnit, RootInput, RootUpdateInput, MeasureWithUnitRepository>
        implements MeasureWithUnitService {

    public MeasureWithUnitServiceImpl(MeasureWithUnitRepository repository) {
    	super(repository);
	}

    protected XtdMeasureWithUnit newEntityInstance() {
        return new XtdMeasureWithUnit();
    }
}
