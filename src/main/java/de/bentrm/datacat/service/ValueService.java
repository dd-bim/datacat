package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdValue;
import de.bentrm.datacat.graphql.dto.ValueInput;
import de.bentrm.datacat.graphql.dto.ValueUpdateInput;

public interface ValueService extends RootService<XtdValue, ValueInput, ValueUpdateInput> { }
