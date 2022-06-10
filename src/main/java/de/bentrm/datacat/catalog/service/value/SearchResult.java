package de.bentrm.datacat.catalog.service.value;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import lombok.Value;

import java.util.List;

@Value
public class SearchResult {

    String id;
    CatalogRecordType recordType;
    List<TagValue> tags;
    String versionId;
    String versionDate;
    String name;
    List<TranslationValue> names;
    List<TranslationValue> descriptions;
    List<TranslationValue> comments;

}
