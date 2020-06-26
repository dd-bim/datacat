package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.domain.Translation;
import de.bentrm.datacat.service.Specification;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    Translation toTranslation(TextInput input);

    Specification toSpecification(SearchInput input);

    Specification toSpecification(FilterInput input);
}
