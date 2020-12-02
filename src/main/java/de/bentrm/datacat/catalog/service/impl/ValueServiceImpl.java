package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.ToleranceType;
import de.bentrm.datacat.catalog.domain.ValueRole;
import de.bentrm.datacat.catalog.domain.ValueType;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.service.ValueService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class ValueServiceImpl extends AbstractServiceImpl<XtdValue> implements ValueService {

    public ValueServiceImpl(SessionFactory sessionFactory, ValueRepository repository) {
        super(XtdValue.class, sessionFactory, repository);
    }

    @Transactional
    @Override
    public XtdValue setTolerance(@NotBlank String id, @NotNull ToleranceType toleranceType,
                                 String lowerTolerance, String upperTolerance) {
        final boolean allBlank = StringUtils.isBlank(lowerTolerance) && StringUtils.isBlank(upperTolerance);

        switch (toleranceType) {
            case Realvalue -> {
                Assert.isTrue(!allBlank,
                        "A real tolerance value must defined a lower and/or upper tolerance boundary.");
            }
            case Percentage -> {
                Assert.isTrue(!allBlank,
                        "A tolerance value given in percentage must defined a lower and/or upper tolerance boundary.");
                if (lowerTolerance != null) {
                    Assert.isTrue(NumberUtils.isParsable(lowerTolerance),
                            "The given lower tolerance isn't a numeric value.");
                }
                if (upperTolerance != null) {
                    Assert.isTrue(NumberUtils.isParsable(upperTolerance),
                            "The given upper tolerance isn't a numeric value.");
                }
            }
        }

        XtdValue item = findById(id).orElseThrow();
        item.setToleranceType(toleranceType);
        item.setLowerTolerance(lowerTolerance != null ? lowerTolerance.trim() : null);
        item.setUpperTolerance(upperTolerance != null ? upperTolerance.trim() : null);

        return getRepository().save(item);
    }

    @Transactional
    @Override
    public XtdValue unsetTolerance(@NotBlank String id) {
        XtdValue item = findById(id).orElseThrow();
        item.setToleranceType(null);
        item.setLowerTolerance(null);
        item.setUpperTolerance(null);

        return getRepository().save(item);
    }

    @Transactional
    @Override
    public XtdValue setNominalValue(@NotBlank String id, @NotNull ValueRole valueRole,
                                    @NotNull ValueType valueType, String nominalValue) {
        Assert.notNull(valueRole, "A valid value role must be provided.");
        Assert.notNull(valueType, "A valid value type must be provided.");
        Assert.isTrue(StringUtils.isNoneBlank(nominalValue), "A given value may not be null or blank.");

        switch (valueType) {
            case Number -> Assert.isTrue(NumberUtils.isParsable(nominalValue), "The given numeric value is not parsable.");
            case Integer -> Long.parseLong(nominalValue);
            case Real -> Double.parseDouble(nominalValue);
            case Boolean -> Boolean.parseBoolean(nominalValue);
        }

        XtdValue item = findById(id).orElseThrow();
        item.setValueRole(valueRole);
        item.setValueType(valueType);
        item.setNominalValue(nominalValue.trim());

        return getRepository().save(item);
    }

    @Transactional
    @Override
    public XtdValue unsetNominalValue(@NotBlank String id) {
        XtdValue item = findById(id).orElseThrow();
        item.setValueRole(null);
        item.setValueType(null);
        item.setNominalValue(null);

        return getRepository().save(item);
    }
}
