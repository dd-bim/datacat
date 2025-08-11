package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.Enums.XtdDataTypeEnum;
import lombok.Data;

@Data
public class PropertyInput {
    XtdDataTypeEnum dataType;
    String dataFormat;
}
