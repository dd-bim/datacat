package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.service.Specification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);
    Specification toSpecification(SearchInput input);
    Specification toSpecification(FilterInput input);
}
