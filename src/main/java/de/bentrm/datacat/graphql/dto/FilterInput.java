package de.bentrm.datacat.graphql.dto;

import lombok.Data;

import java.util.List;

@Data
public class FilterInput {
    String query = null;
    List<String> idIn;
    List<String> idNotIn;
    List<String> tagged;
    Integer pageNumber = 0;
    Integer pageSize = 10;
}
