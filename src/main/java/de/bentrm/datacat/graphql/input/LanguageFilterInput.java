package de.bentrm.datacat.graphql.input;

import lombok.Data;

import java.util.List;

@Data
public class LanguageFilterInput {
    String query;
    List<String> excludeLanguageTags;


}
