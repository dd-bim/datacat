package de.bentrm.datacat.graphql.input;

import lombok.Data;

@Data
public class ExternalDocumentInput {
    String documentUri;
    String author;
    String isbn;
    String publisher;
    String dateOfPublication;
}
