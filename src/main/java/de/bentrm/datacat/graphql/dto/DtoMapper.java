package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.service.Specification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    Specification toSpecification(SearchInput input);
    Specification toSpecification(FilterInput input);
}
