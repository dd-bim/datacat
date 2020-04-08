package de.bentrm.datacat.query;

import javax.validation.constraints.NotNull;

public interface CustomQuery {

    @NotNull String getQueryTemplate();

    @NotNull String getPropertyAggregations();

}
