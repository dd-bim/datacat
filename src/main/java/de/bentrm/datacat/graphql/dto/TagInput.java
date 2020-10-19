package de.bentrm.datacat.graphql.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagInput {

    private String scope;
    private List<LocalizedTextInput> names = new ArrayList<>();
    private List<LocalizedTextInput> descriptions = new ArrayList<>();

}
