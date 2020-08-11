package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.domain.EntityType;
import lombok.Data;

import java.util.List;

@Data
public class SearchInput {
    private String query = null;
    private List<EntityType> entityTypeIn;
    private List<EntityType> entityTypeNotIn;
    private List<String> idIn;
    private List<String> idNotIn;
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
}
