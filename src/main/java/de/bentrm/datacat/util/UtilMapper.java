package de.bentrm.datacat.util;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.neo4j.ogm.model.QueryStatistics;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UtilMapper {
    UtilMapper INSTANCE = Mappers.getMapper(UtilMapper.class);

    QueryStatisticsWrapper toWrapper(QueryStatistics statistics);
}
