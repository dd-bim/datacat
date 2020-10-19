package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.catalog.domain.ToleranceType;

public interface ToleranceComponentInput {

    ToleranceType getToleranceType();

    String getLowerTolerance();

    String getUpperTolerance();
}
