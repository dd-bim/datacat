package de.bentrm.datacat.graphql.input;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.Enums.XtdStatusOfActivationEnum;

import java.util.List;

@Data
public class CatalogEntryPropertiesInput {
    String id;
    int majorVersion = 1;
    int minorVersion = 0;
    String dateOfCreation;
    XtdStatusOfActivationEnum status = XtdStatusOfActivationEnum.XTD_ACTIVE;
    List<@NotNull @Valid TranslationInput> names;
    List<@NotNull @Valid TranslationInput> descriptions;
    List<@NotNull @Valid TranslationInput> comments;
    List<@NotNull@Valid TranslationInput> deprecationExplanation;

    @Valid PropertyInput propertyProperties;

    @Valid UnitInput unitProperties;

    @Valid ExternalDocumentInput externalDocumentProperties;

    @Valid CountryInput countryProperties;

    @Valid CountryInput subdivisionProperties;

    @Valid ValueInput valueProperties;

    @Valid OrderedValueInput orderedValueProperties;

    @Valid IntervalInput intervalProperties;

    @Valid LanguageInput languageProperties;

    @Valid TextInput textProperties;

    @Valid RationalInput rationalProperties;

    @Valid ValueListInput valueListProperties;

    @Valid SymbolInput symbolProperties;

}
