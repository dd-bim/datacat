package de.bentrm.datacat.graphql.input;

import lombok.Data;

@Data
public class ExternalDocumentInput {
    String uri;
    String author;
    String isbn;
    String publisher;
}
