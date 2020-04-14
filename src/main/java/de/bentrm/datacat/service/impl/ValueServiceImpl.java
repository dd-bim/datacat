package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdValue;
import de.bentrm.datacat.graphql.dto.ValueInput;
import de.bentrm.datacat.graphql.dto.ValueUpdateInput;
import de.bentrm.datacat.repository.ValueRepository;
import de.bentrm.datacat.service.ValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class ValueServiceImpl
        extends RootServiceImpl<XtdValue, ValueInput, ValueUpdateInput, ValueRepository>
        implements ValueService {

    public ValueServiceImpl(ValueRepository repository) {
        super(repository);
    }

    @Override
    protected XtdValue newEntityInstance() {
        return new XtdValue();
    }

    @Override
    protected void setEntityProperties(XtdValue entity, ValueInput dto) {
        entity.setToleranceType(dto.getToleranceType());
        entity.setLowerTolerance(dto.getLowerTolerance());
        entity.setUpperTolerance(dto.getUpperTolerance());

        entity.setValueRole(dto.getValueRole());
        entity.setValueType(dto.getValueType());
        entity.setNominalValue(dto.getNominalValue());

        super.setEntityProperties(entity, dto);
    }

    @Override
    protected void updateEntityProperties(XtdValue entity, ValueUpdateInput dto) {
        entity.setToleranceType(dto.getToleranceType());
        entity.setLowerTolerance(dto.getLowerTolerance());
        entity.setUpperTolerance(dto.getUpperTolerance());

        entity.setValueRole(dto.getValueRole());
        entity.setValueType(dto.getValueType());
        entity.setNominalValue(dto.getNominalValue());

        super.updateEntityProperties(entity, dto);
    }
}
