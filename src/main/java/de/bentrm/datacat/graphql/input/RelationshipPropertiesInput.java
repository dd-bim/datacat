package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

import de.bentrm.datacat.catalog.domain.Enums.XtdStatusOfActivationEnum;

import java.util.List;

@Data
public class RelationshipPropertiesInput {
    String id;
    int majorVersion = 1;
    int minorVersion = 0;
    XtdStatusOfActivationEnum status = XtdStatusOfActivationEnum.XTD_ACTIVE;
    @NotEmpty List<@NotNull @Valid TranslationInput> names;
    List<@NotNull @Valid TranslationInput> descriptions;
    List<@NotNull @Valid TranslationInput> comments;

    @Valid RelationshipToPropertyInput relationshipToPropertyProperties;
}
