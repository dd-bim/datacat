package de.bentrm.datacat.graphql.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.catalog.domain.CatalogEntryType;
import de.bentrm.datacat.catalog.domain.EntityType;
import de.bentrm.datacat.catalog.service.value.*;
import org.apache.commons.lang3.LocaleUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import org.mapstruct.factory.Mappers;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ApiInputMapper {

    ApiInputMapper INSTANCE = Mappers.getMapper(ApiInputMapper.class);

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default SearchInput toSearchInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, SearchInput.class);
    }

    default LocaleInput toLocaleInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, LocaleInput.class);
    }

    default LocalizationInput toLocalizationInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, LocalizationInput.class);
    }

    default CreateEntryInput toCreateEntryInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, CreateEntryInput.class);
    }

    default DeleteCatalogEntryInput toDeleteEntryInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, DeleteCatalogEntryInput.class);
    }

    default DeleteRelationshipInput toDeleteRelationshipInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, DeleteRelationshipInput.class);
    }

    default SetVersionInput toSetVersionInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, SetVersionInput.class);
    }

    default AddNameInput toAddNameInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, AddNameInput.class);
    }

    default UpdateNameInput toUpdateNameInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, UpdateNameInput.class);
    }

    default DeleteNameInput toDeleteNameInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, DeleteNameInput.class);
    }

    default AddDescriptionInput toAddDescriptionInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, AddDescriptionInput.class);
    }

    default UpdateDescriptionInput toUpdateDescriptionInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, UpdateDescriptionInput.class);
    }

    default DeleteDescriptionInput toDeleteDescriptionInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, DeleteDescriptionInput.class);
    }

    default SetToleranceInput toSetToleranceInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, SetToleranceInput.class);
    }

    default UnsetToleranceInput toUnsetToleranceInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, UnsetToleranceInput.class);
    }

    default SetNominalValueInput toSetNominalValueInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, SetNominalValueInput.class);
    }

    default UnsetNominalValueInput toUnsetNominalValueInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, UnsetNominalValueInput.class);
    }

    default HierarchyFilterInput toHierarchyFilterInput(Map<String, Object> arguments) {
        return OBJECT_MAPPER.convertValue(arguments, HierarchyFilterInput.class);
    }

    CatalogEntryProperties toEntryValue(EntryPropertiesInput input);

    OneToOneRelationshipValue toValue(CreateOneToOneRelationshipInput input);

    OneToManyRelationshipValue toValue(CreateOneToManyRelationshipInput input);

    QualifiedOneToManyRelationshipValue toValue(CreateQualifiedOneToManyRelationshipInput input);

    default List<TranslationValue> toTranslationValue(List<TranslationInput> inputs) {
        if (inputs == null) {
            return null;
        }
        return inputs.stream().map(this::toTranslationValue).collect(Collectors.toList());
    }

    default TranslationValue toTranslationValue(TranslationInput input) {
        Locale locale = new Locale.Builder().setLanguageTag(input.getLanguageTag()).build();
        Assert.isTrue(LocaleUtils.isAvailableLocale(locale), String.format("Illegal locale provided: %s", input.getLanguageTag()));
        return new TranslationValue(input.getId(), locale, input.getValue());
    }

    @ValueMappings({
            @ValueMapping(source = "Actor", target = "XtdActor"),
            @ValueMapping(source = "Activity", target = "XtdActivity"),
            @ValueMapping(source = "Bag", target = "XtdBag"),
            @ValueMapping(source = "Classification", target = "XtdClassification"),
            @ValueMapping(source = "ExternalDocument", target = "XtdExternalDocument"),
            @ValueMapping(source = "Measure", target = "XtdMeasureWithUnit"),
            @ValueMapping(source = "Nest", target = "XtdNest"),
            @ValueMapping(source = "Subject", target = "XtdSubject"),
            @ValueMapping(source = "Property", target = "XtdProperty"),
            @ValueMapping(source = "Unit", target = "XtdUnit"),
            @ValueMapping(source = "Value", target = "XtdValue")
    })
    EntityType entryTypeToEntityType(CatalogEntryType catalogEntryType);

}
