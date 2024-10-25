package de.bentrm.datacat.graphql.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.catalog.service.value.TranslationValue;
import de.bentrm.datacat.graphql.input.verification.*;
import org.apache.commons.lang3.LocaleUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
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
    
    default CreateRelationshipInput toCreateRelationshipInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, CreateRelationshipInput.class);
    }

    default DeleteRelationshipInput toDeleteRelationshipInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, DeleteRelationshipInput.class);
    }

    default DeleteObjectRelationshipInput toDeleteObjectRelationshipInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, DeleteObjectRelationshipInput.class);
    }


    default UpdateDescriptionInput toUpdateDescriptionInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, UpdateDescriptionInput.class);
    }

    default DeleteDescriptionInput toDeleteDescriptionInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, DeleteDescriptionInput.class);
    }

    default UpdateCommentInput toUpdateCommentInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, UpdateCommentInput.class);
    }

    default DeleteCommentInput toDeleteCommentInput(Map<String, Object> argument) {
        return OBJECT_MAPPER.convertValue(argument, DeleteCommentInput.class);
    }

    default HierarchyFilterInput toHierarchyFilterInput(Map<String, Object> arguments) {
        return OBJECT_MAPPER.convertValue(arguments, HierarchyFilterInput.class);
    }

    default findMissingTagsFilterInput tofindMissingTagsFilterInput(Map<String, Object> arguments) {
        return OBJECT_MAPPER.convertValue(arguments, findMissingTagsFilterInput.class);
    }

    default findMissingEnglishNameFilterInput tofindMissingEnglishNameFilterInput(Map<String, Object> arguments) {
        return OBJECT_MAPPER.convertValue(arguments, findMissingEnglishNameFilterInput.class);
    }

    default findMultipleIDsFilterInput tofindMultipleIDsFilterInput(Map<String, Object> arguments) {
        return OBJECT_MAPPER.convertValue(arguments, findMultipleIDsFilterInput.class);
    }

    default findMissingDescriptionFilterInput tofindMissingDescriptionFilterInput(Map<String, Object> arguments) {
        return OBJECT_MAPPER.convertValue(arguments, findMissingDescriptionFilterInput.class);
    }

    default findMissingEnglishDescriptionFilterInput tofindMissingEnglishDescriptionFilterInput(Map<String, Object> arguments) {
        return OBJECT_MAPPER.convertValue(arguments, findMissingEnglishDescriptionFilterInput.class);
    }

    default findMultipleNamesFilterInput tofindMultipleNamesFilterInput(Map<String, Object> arguments) {
        return OBJECT_MAPPER.convertValue(arguments, findMultipleNamesFilterInput.class);
    }

    default findMultipleNamesAcrossClassesFilterInput tofindMultipleNamesAcrossClassesFilterInput(Map<String, Object> arguments) {
        return OBJECT_MAPPER.convertValue(arguments, findMultipleNamesAcrossClassesFilterInput.class);
    }

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
}
