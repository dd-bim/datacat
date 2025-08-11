package de.bentrm.datacat.graphql.input;

import java.util.List;

import lombok.Data;

@Data
public class ExternalDocumentInput {
    String documentUri;
    String author;
    String isbn;
    String publisher;
    String dateOfPublication;
    List<String> languageTag;
}
