package de.bentrm.datacat.graphql.input;

import lombok.Data;
import java.util.List;

@Data
public class LanguageInput {
    String englishName;
    String nativeName;
    String code;
    List<String> comments;
}
